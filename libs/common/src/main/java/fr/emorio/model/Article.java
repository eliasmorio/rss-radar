package fr.emorio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@Entity(name = "RR_ARTICLE")
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column(columnDefinition = "TEXT", unique = true)
    private String link;
    @Column(name = "publication_date")
    private LocalDateTime publicationDate;
    @Column(length = 50, nullable = false, columnDefinition = "REQCONFIG")
    private String language;
    @ManyToOne
    private Feed feed;

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
}
