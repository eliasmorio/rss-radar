package fr.emorio.service;

import fr.emorio.parser.FeedParserFactory;
import fr.emorio.model.Article;
import fr.emorio.model.Feed;

import fr.emorio.parser.FeedParser;
import fr.emorio.repository.ArticleRepository;
import fr.emorio.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;


@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {
    private static final String USER_AGENT = "Mozilla/5.0";
    private final FeedRepository feedRepository;
    private final ArticleRepository articleRepository;
    private final FeedParserFactory feedParserFactory;
    private final RabbitTemplate rabbitTemplate;
    private final LanguageDetectionService languageDetectionService;
    
    public void crawl(Long feedId) throws RuntimeException, IOException {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("Feed not found"));
        Document feedContent = fetchFeedContent(feed.getUrl());

        FeedParser feedParser = feedParserFactory.getFeedParser(feedContent);

        if (isFirstFetch(feed)) {
            Feed parsedFeed = feedParser.parseFeedMetadata(feedContent);
            feed.setTitle(parsedFeed.getTitle());
            feed.setDescription(parsedFeed.getDescription());
            feed.setLocale(parsedFeed.getLocale());
        }

        Set<Article> articles = feedParser.parseFeed(feedContent);

        for (Article article : articles) {
            article.setLanguage(languageDetectionService.detectLanguage(article));
        }

        log.info("Found {} articles for feed {}", articles.size(), feed);

        articles = articles.stream()
                    .filter(article -> !articleRepository.existsByLink(article.getLink()))
                    .collect(Collectors.toSet());
        articles.forEach(article -> article.setFeed(feed));
        articles.forEach(this::trySaveArticle);

        log.info("Found {} new articles for feed {}", articles.size(), feed);
        articles.forEach(this::explore);
        
        feed.setLastFetchDate(LocalDateTime.now(UTC));
        feedRepository.save(feed);
    }

    private Document fetchFeedContent(String url) throws IOException {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .get();
        }
        catch (IOException e) {
            log.error("Error while fetching feed content from {}", url, e);
            throw e;
        }
    }

    private void explore(Article article) {
        rabbitTemplate.convertAndSend("explore-queue", article.getId());
    }

    private boolean isFirstFetch(Feed feed) {
        return feed.getLastFetchDate() == null;
    }

    private void trySaveArticle(Article article) {
        try {
            articleRepository.save(article);
        } catch (Exception e) {
            log.error("Error while saving article {}", article, e);
        }
    }
    

}
