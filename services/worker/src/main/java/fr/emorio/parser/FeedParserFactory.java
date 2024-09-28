package fr.emorio.parser;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class FeedParserFactory {
    
    public FeedParser getFeedParser(Document feedContent) {
        if (!feedContent.select("rss").isEmpty()) {
            return new RssFeedParser();
        } else if (!feedContent.select("feed").isEmpty()) {
            return new AtomFeedParser();
        } else {
            throw new IllegalArgumentException("Feed type not supported");
        }
    }


}
