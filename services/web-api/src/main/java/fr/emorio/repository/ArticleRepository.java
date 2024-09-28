package fr.emorio.repository;

import fr.emorio.model.Article;
import jakarta.persistence.NamedNativeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

    @Query(
            value = """
            SELECT * FROM rr_article article
            WHERE to_tsvector(article.title || ' ' || article.body) @@ to_tsquery(:query) 
        """,
            countQuery = """
            SELECT COUNT(*) FROM rr_article article
            WHERE to_tsvector(article.title || ' ' || article.body) @@ to_tsquery(:query) 
        """,
            nativeQuery = true)
    Page<Article> searchArticlesByPage(String query,

                                       Pageable pageable);

}
