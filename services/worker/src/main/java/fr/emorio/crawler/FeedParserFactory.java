package fr.emorio.crawler;

import fr.emorio.model.AtomFeed;
import fr.emorio.model.Feed;
import fr.emorio.model.RssFeed;
import fr.emorio.parser.AtomFeedParser;
import fr.emorio.parser.FeedParser;
import fr.emorio.parser.RssFeedParser;

public class FeedParserFactory {
    
    public FeedParser getFeedParser(Feed feed) {
        if (feed instanceof AtomFeed) {
            return new AtomFeedParser();
        } else if (feed instanceof RssFeed) {
            return new RssFeedParser();
        } else {
            throw new IllegalArgumentException("Unknown feed type");
        }
    }
}
