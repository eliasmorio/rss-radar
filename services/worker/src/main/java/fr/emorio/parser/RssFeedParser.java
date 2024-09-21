package fr.emorio.parser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static java.time.ZoneOffset.UTC;

public class RssFeedParser extends FeedParser {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");
    
    @Override
    protected Set<Element> parseNodeArticles(Element rawContent) {
        Set<Element> articles = new HashSet<>();
        NodeList nodeList = rawContent.getElementsByTagName("item");
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
        return rawContent.getElementsByTagName("description").item(0).getTextContent();
    }

    @Override
    protected LocalDateTime parsePublicationDate(Element rawContent) {
        var zonedDateTime = ZonedDateTime.parse(rawContent.getElementsByTagName("pubDate").item(0).getTextContent(), DATE_FORMATTER);
        // to UTC
        return zonedDateTime.withZoneSameInstant(UTC).toLocalDateTime();
    }

    @Override
    protected String parseLink(Element rawContent) {
        return rawContent.getElementsByTagName("link").item(0).getTextContent();
    }
}
