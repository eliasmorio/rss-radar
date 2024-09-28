package fr.emorio.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Slf4j
public class AtomFeedParser extends FeedParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    protected String parseFeedTitle(Document feedContent) {
        Element titleElement = feedContent.selectFirst("title");
        return titleElement != null ? titleElement.text() : "";
    }

    @Override
    protected String parseFeedDescription(Document feedContent) {
        Element subtitleElement = feedContent.selectFirst("subtitle");
        return subtitleElement != null ? subtitleElement.text() : "";
    }

    @Override
    protected Locale parseFeedLocale(Document feedContent) {
        return Locale.getDefault();
    }

    @Override
    protected Set<Element> parseArticles(Document rawContent) {
        return new HashSet<>(rawContent.select("entry"));
    }


    @Override
    protected String parseArticleTitle(Element rawContent) {
        Element titleElement = rawContent.selectFirst("title");
        return titleElement != null ? titleElement.text() : "";
    }

    @Override
    protected String parseArticleBody(Element rawContent) {
        Element contentElement = rawContent.selectFirst("content");
        return contentElement != null ? contentElement.text() : "";
    }

    @Override
    protected LocalDateTime parseArticlePublicationDate(Element rawContent) {
        try {
            Element updatedElement = rawContent.selectFirst("updated");
            if (updatedElement != null) {
                return LocalDateTime.parse(updatedElement.text(), DATE_FORMATTER);
            }
        } catch (Exception e) {
            log.warn("Error while parsing publication date for article: {}", rawContent.toString());
        }
        return LocalDateTime.now();
    }

    @Override
    protected String parseArticleLink(Element rawContent) {
        Element linkElement = rawContent.selectFirst("link[href]");
        return linkElement != null ? linkElement.attr("href") : "";
    }

}
