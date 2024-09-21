package fr.emorio.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
public class Article {
    private Long id;
    private String title;
    private String body;
    private String link;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title) && Objects.equals(body, article.body) && Objects.equals(link, article.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body, link);
    }

    private LocalDateTime publicationDate;
    private Feed feedSource;
    
    
}
