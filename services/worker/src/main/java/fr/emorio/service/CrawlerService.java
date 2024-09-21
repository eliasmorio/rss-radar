package fr.emorio.service;

import fr.emorio.crawler.FeedParserFactory;
import fr.emorio.model.Article;
import fr.emorio.model.Feed;

import fr.emorio.parser.FeedParser;
import fr.emorio.repository.ArticleRepository;
import fr.emorio.repository.FeedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Set;

import static java.time.ZoneOffset.UTC;


@Slf4j
@Service
public class CrawlerService {
    private final FeedRepository feedRepository;
    private final ArticleRepository articleRepository;
    private final FeedParserFactory feedParserFactory;
    
    public CrawlerService(FeedRepository feedRepository, ArticleRepository articleRepository) {
        this.feedRepository = feedRepository;
        this.articleRepository = articleRepository;
        this.feedParserFactory = new FeedParserFactory();
    }
    
    public void crawl(Long feedId)  throws RuntimeException {
        String feedContent;
        Feed feed = feedRepository.findById(feedId).orElseThrow();
        
        try {
            feedContent = fetchFeedContent(feed.getUrl());
        }
        catch (IOException e) {
            throw new RuntimeException("Error while fetching feed content", e);
        }
      
        Set<Article> articles = parseArticles(feedContent, feed);
        articles.forEach(this::trySaveArticle);
        
        feed.setLastFetchDate(LocalDateTime.now(UTC)); 
    }
    
    public void trySaveArticle(Article article) {
        try {
            articleRepository.save(article);
        } catch (DataIntegrityViolationException e) {
            if (!e.getMessage().contains("rr_article_hash_key")) {
                throw e;
            }
            log.info("Article already exists: {}", article.getLink());
        }
    }
    
    public Set<Article> parseArticles(String feedContent, Feed feed) {
        FeedParser feedParser = feedParserFactory.getFeedParser(feed);
        Set<Article> articles = feedParser.parseArticles(feedContent);
        //attach feed to each article
        articles.forEach(article -> article.setFeedSource(feed));
        return articles;
    }
    
    private String fetchFeedContent(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error while fetching feed content: " + responseCode);
        }
        
        byte[] content = connection.getInputStream().readAllBytes();
        return new String(content);
    }
    
}
