package fr.emorio.parser;

import fr.emorio.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class FeedParser {
    
    public Set<Article> parseArticles(String rawContent) {
        Document document = parseDocument(rawContent);

        if (document == null) {
            return new HashSet<>();
        }

        Set<Article> articles = new HashSet<>();
        Set<Element> nodeArticles = parseNodeArticles(document.getDocumentElement());
        for (Element nodeArticle : nodeArticles) {
            try {
                articles.add(parseArticle(nodeArticle));
            } catch (Exception e) {
                log.error("Error while parsing article {}", nodeArticle, e);
            }
        }
        return articles;
    }

    public Article parseArticle(Element rawContent) {
        return Article.builder()
                        .title(parseTitle(rawContent).trim())
                        .body(parseBody(rawContent).trim())
                        .publicationDate(parsePublicationDate(rawContent))
                        .link(parseLink(rawContent))
                        .build();
    }

    protected abstract Set<Element> parseNodeArticles(Element rawContent);

    protected abstract String parseTitle(Element rawContent);

    protected abstract String parseBody(Element rawContent);

    protected abstract LocalDateTime parsePublicationDate(Element rawContent);
    
    protected abstract String parseLink(Element rawContent);

    protected Document parseDocument(String rawContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new java.io.ByteArrayInputStream(rawContent.getBytes()));
        } catch (Exception e) {
            log.error("Error while parsing document", e);
            return null;
        }
    }

}
