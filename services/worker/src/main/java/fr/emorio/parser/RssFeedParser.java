package fr.emorio.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


@Slf4j
public class RssFeedParser extends FeedParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Override
    protected String parseFeedTitle(Document feedContent) {
        Element titleElement = feedContent.selectFirst("title");
        return titleElement != null ? titleElement.text() : "";
    }

    @Override
    protected String parseFeedDescription(Document feedContent) {
        Element descriptionElement = feedContent.selectFirst("description");
        return descriptionElement != null ? descriptionElement.text() : "";
    }

    @Override
    protected Locale parseFeedLocale(Document feedContent) {
        Element languageElement = feedContent.selectFirst("language");
        return languageElement != null ? Locale.forLanguageTag(languageElement.text()) : Locale.getDefault();
    }

    @Override
    protected Set<Element> parseArticles(Document rawContent) {
        return new HashSet<>(rawContent.select("item"));
    }

    @Override
    protected String parseArticleTitle(Element rawContent) {
        Element titleElement = rawContent.selectFirst("title");
        return titleElement != null ? titleElement.text() : "";
    }

    @Override
    protected String parseArticleBody(Element rawContent) {
        Element descriptionElement = rawContent.selectFirst("description");
        return descriptionElement != null ? descriptionElement.text() : "";
    }

    @Override
    protected LocalDateTime parseArticlePublicationDate(Element rawContent) {
        try {
            Element pubDateElement = rawContent.selectFirst("pubDate");
            if (pubDateElement != null) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDateElement.text(), DATE_FORMATTER);
                return zonedDateTime.withZoneSameInstant(UTC).toLocalDateTime(); // Convert to UTC
            }
        } catch (Exception e) {
            log.warn("Error while parsing publication date for article: {}", rawContent.toString());
        }
        return LocalDateTime.now();
    }

    @Override
    protected String parseArticleLink(Element rawContent) {
        Element linkElement = rawContent.selectFirst("link");
        return linkElement != null ? linkElement.text() : "";
    }

}
