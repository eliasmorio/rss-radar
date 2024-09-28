package fr.emorio.service;

import fr.emorio.model.Article;
import fr.emorio.model.Feed;
import fr.emorio.repository.ArticleRepository;
import fr.emorio.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExplorerService {
    private static final String RSS_MIME_TYPE = "link[type=application/rss+xml]";
    private static final String ATOM_MIME_TYPE = "link[type=application/atom+xml]";

    private final FeedRepository feedRepository;
    private final ArticleRepository articleRepository;
    private final RabbitTemplate rabbitTemplate;

    public void explore(Long articleId) {
        Article article = articleRepository.findById(articleId).
                orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
        try {
            log.info("Exploring article {}", article);
            explore(article);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void explore(Article article) throws IOException {
        Document document = Jsoup.connect(article.getLink()).get();

        List<Element> rssLinks = document.select(RSS_MIME_TYPE);
        List<Element> atomLinks = document.select(ATOM_MIME_TYPE);

        //merge rssLinks and atomLinks
        rssLinks.addAll(atomLinks);

        for (Element link : rssLinks) {
            String feedUrl = link.attr("href");
            if (feedRepository.existsByUrl(feedUrl)) {
                continue;
            }

            Feed feed = Feed.builder()
                    .url(feedUrl)
                    .build();
            feedRepository.save(feed);
            rabbitTemplate.convertAndSend("crawl-queue", feed.getId());
        }

    }

}
