package fr.emorio.parser;

import fr.emorio.model.Article;
import fr.emorio.model.Feed;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Slf4j
public abstract class FeedParser {
    
    public Set<Article> parseFeed(Document feedContent) {
        Set<Article> articles = new HashSet<>();
        Set<Element> nodeArticles = parseArticles(feedContent);
        for (Element nodeArticle : nodeArticles) {
            try {
                articles.add(parseArticle(nodeArticle));
            } catch (Exception e) {
                log.error("Error while parsing article {}", nodeArticle, e);
            }
        }
        return articles;
    }

    public Feed parseFeedMetadata(Document feedContent) {
        return Feed.builder()
                    .title(parseFeedTitle(feedContent))
                    .description(parseFeedDescription(feedContent))
                    .locale(parseFeedLocale(feedContent))
                    .build();
    }

    protected abstract String parseFeedTitle(Document feedContent);

    protected abstract String parseFeedDescription(Document feedContent);

    protected abstract Locale parseFeedLocale(Document feedContent);

    public Article parseArticle(Element rawContent) {
        return Article.builder()
                        .title(parseArticleTitle(rawContent).trim())
                        .body(parseArticleBody(rawContent).trim())
                        .publicationDate(parseArticlePublicationDate(rawContent))
                        .link(parseArticleLink(rawContent))
                        .build();
    }

    protected abstract Set<Element> parseArticles(Document rawContent);

    protected abstract String parseArticleTitle(Element rawContent);

    protected abstract String parseArticleBody(Element rawContent);

    protected abstract LocalDateTime parseArticlePublicationDate(Element rawContent);
    
    protected abstract String parseArticleLink(Element rawContent);

}
