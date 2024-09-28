package fr.emorio.repository;

import fr.emorio.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

    @Query(
            value = """
            SELECT * FROM rr_article article
            WHERE to_tsvector(article.title || ' ' || article.body) @@ to_tsquery(:query)\s
       \s""",
            countQuery = """
            SELECT COUNT(*) FROM rr_article article
            WHERE to_tsvector(article.title || ' ' || article.body) @@ to_tsquery(:query)\s
       \s""",
            nativeQuery = true)
    Page<Article> searchArticlesByPage(String query,

                                       Pageable pageable);

}
