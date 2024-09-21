package fr.emorio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@SuperBuilder
public class Feed {
    
    private Long id;
    private URL url;
    private String title;
    private String description;
    private Locale locale;
    private LocalDateTime lastFetchDate;
    private List<Article> articles;
}

