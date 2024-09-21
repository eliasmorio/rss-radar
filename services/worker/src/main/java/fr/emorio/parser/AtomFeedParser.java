package fr.emorio.parser;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AtomFeedParser extends FeedParser {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+00:00";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Override
    protected Set<Element> parseNodeArticles(Element rawContent) {
        Set<Element> articles = new HashSet<>();
        NodeList nodeList = rawContent.getElementsByTagName("entry");
        for (int i = 0; i < nodeList.getLength(); i++) {
            articles.add((Element) nodeList.item(i));
        }
        return articles;
    }

    @Override
    protected String parseTitle(Element rawContent) {
        return rawContent.getElementsByTagName("title").item(0).getTextContent();
    }

    @Override
    protected String parseBody(Element rawContent) {
        return rawContent.getElementsByTagName("content").item(0).getTextContent();
    }

    @Override
    protected LocalDateTime parsePublicationDate(Element rawContent) {
        try {
            return LocalDateTime.parse(rawContent.getElementsByTagName("updated").item(0).getTextContent(), DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("Error while parsing publication date for article: {}", rawContent.toString());
            return LocalDateTime.now();
        }
    }
    
    @Override
    protected String parseLink(Element rawContent) {
        return rawContent.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getTextContent();
    }

}
