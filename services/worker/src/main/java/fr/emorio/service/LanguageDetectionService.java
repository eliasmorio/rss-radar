package fr.emorio.service;

import fr.emorio.model.Article;
import lombok.RequiredArgsConstructor;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LanguageDetectionService {
    private final LanguageDetector languageDetector;

    public String detectLanguage(Article article) {
        if (article == null) {
            return null;
        }
        String text = article.getBody() + " " + article.getTitle();
        text = removeHtmlTags(text);
        text = removeUppercaseWords(text);


        LanguageResult languageResult = detectLanguage(text);
        return toDisplayLanguage(languageResult.getLanguage());
    }

    private String removeHtmlTags(String text) {
        return text.replaceAll("<.*?>", "");
    }

    private String removeUppercaseWords(String text) {
        return text.replaceAll("\\b[A-Z]\\w+\\b", "");
    }

    public LanguageResult detectLanguage(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return languageDetector.detect(text);
    }

    private String toDisplayLanguage(String langCode) {
        Locale locale = new Locale(langCode);
        return locale.getDisplayLanguage(Locale.ENGLISH).toLowerCase();
    }

}
