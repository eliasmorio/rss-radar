package fr.emorio.repository;

import fr.emorio.model.Article;
import fr.emorio.model.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepository  {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Article> rowMapper;

    public ArticleRepository(JdbcTemplate jdbcTemplate, FeedRepository feedRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rs, rowNum) -> {
            Article article = Article.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .body(rs.getString("body"))
                    .link(rs.getString("link"))
                    .publicationDate(rs.getTimestamp("publication_date").toLocalDateTime())
                    .build();

            Long feedId = rs.getLong("feed_id");
            Feed feed = feedRepository.findById(feedId).orElse(null);
            article.setFeedSource(feed);

            return article;
        };
    }

    public Page<Article> searchArticlesByPage(Pageable pageable, String query) {
        Integer total = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM rr_article
                WHERE to_tsvector(title || ' ' || body) @@ to_tsquery(?)
            """,
                Integer.class,
                query
        );
        if (total == null) {
            return Page.empty();
        }
        
        List<Article> articles = jdbcTemplate.query("""
                SELECT * FROM rr_article article
                JOIN rr_feed feed ON article.feed_id = feed.id
                WHERE to_tsvector(article.title || ' ' || article.body) @@ to_tsquery(?)
                ORDER BY publication_date DESC LIMIT ? OFFSET ?
            """,
                rowMapper,
                query,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        return new PageImpl<>(articles, pageable, total);
    }
    
}
