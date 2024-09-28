package fr.emorio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity(name = "RR_FEED")
public class Feed {

    @Id
//    @SequenceGenerator(
//            name = "rr_feed_seq",
//            sequenceName = "rr_feed_seq",
//            allocationSize = 1
//    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String url;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Locale locale;
    private LocalDateTime lastFetchDate;
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Article> articles;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Article sourceArticle;

}

