package fr.emorio.repository;

import fr.emorio.model.AtomFeed;
import fr.emorio.model.Feed;
import fr.emorio.model.RssFeed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FeedRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Feed> rowMapper = (rs, rowNum) -> {
        String feedType = rs.getString("feed_class");
        try {
            if (feedType.equals(RssFeed.class.getSimpleName())) {
                return buildFeed(rs, RssFeed.builder());
            } else if (feedType.equals(AtomFeed.class.getSimpleName())) {
                return buildFeed(rs, AtomFeed.builder());
            } else {
                throw new IllegalArgumentException("Unknown feed type: " + feedType);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private Feed buildFeed(ResultSet rs, Feed.FeedBuilder<?, ?> feedBuilder) throws Exception {
        return feedBuilder
                .id(rs.getLong("id"))
                .url(new URI(rs.getString("url")).toURL())
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .locale(Locale.forLanguageTag(rs.getString("locale")))
                .lastFetchDate(Optional.ofNullable(rs.getTimestamp("last_fetch_date"))
                        .map(Timestamp::toLocalDateTime)
                        .orElse(null))
                .build();
    }

    public Optional<Feed> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM RR_FEED WHERE id = ?", rowMapper, id)
                .stream()
                .findFirst();
    }
}
