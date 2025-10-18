package fr.emorio;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SchedulerApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SchedulerApplication.class, args)));
    }
}
