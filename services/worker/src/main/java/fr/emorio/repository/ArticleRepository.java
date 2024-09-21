package fr.emorio.repository;

import fr.emorio.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    public Article save(Article article) {
        Long id = jdbcTemplate.queryForObject("""
                INSERT INTO RR_ARTICLE (title, body, link, publication_date, feed_id, hash)\s
                VALUES (?, ?, ?, ?, ?, ?) RETURNING id
           \s""",
                Long.class,
                article.getTitle(),
                article.getBody(),
                article.getLink(),
                article.getPublicationDate(),
                article.getFeedSource().getId(),
                article.hashCode()
        );
        article.setId(id);
        return article;
    }


}
