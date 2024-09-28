package fr.emorio.service;

import fr.emorio.model.Article;
import fr.emorio.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Page<Article> searchArticles(Pageable pageRequest, String query) {
        return articleRepository.searchArticlesByPage(query, pageRequest);
    }

}
