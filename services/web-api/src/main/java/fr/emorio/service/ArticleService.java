package fr.emorio.service;

import fr.emorio.model.Article;
import fr.emorio.model.Feed;
import fr.emorio.repository.ArticleRepository;
import fr.emorio.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    private final FeedRepository feedRepository;

    public Page<Article> searchArticles(Pageable pageRequest, String query) {
        Page<Article> articles = articleRepository.searchArticlesByPage(pageRequest, query);
        return articles.map(this::updateFeedSource);
    }

    private Article updateFeedSource(Article article) {
        Feed feed = feedRepository.findById(article.getFeedSource().getId()).orElse(null);
        article.setFeedSource(feed);
        return article;
    }
}
