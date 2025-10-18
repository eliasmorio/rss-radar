package fr.emorio;


import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public LanguageDetector languageDetector() throws IOException {
        LanguageDetector detector = new OptimaizeLangDetector().loadModels();
        detector.loadModels();
        return detector;
    }
}
