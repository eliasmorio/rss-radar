package fr.emorio.repository;

import fr.emorio.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    boolean existsByLink(String link);
}
