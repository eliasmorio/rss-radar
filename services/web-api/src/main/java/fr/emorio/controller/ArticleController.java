package fr.emorio.controller;

import fr.emorio.model.Article;
import fr.emorio.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/articles", produces = "application/json")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "${cross.origin.url}")
@Slf4j
public class ArticleController {
    
    private final ArticleService articleService;


    @GetMapping("/search")
    public ResponseEntity<Page<Article>> searchArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "publication_date,desc") String[] sort,
            @RequestParam String query
    ) {
        if (query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Searching articles with query: {}", query);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort[0])));
        Page<Article> articles = articleService.searchArticles(pageRequest, query);
        return ResponseEntity.ok(articles);
    }
    
}
