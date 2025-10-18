package fr.emorio.config;

import fr.emorio.repository.FeedRepository;
import fr.emorio.service.FeedRescheduleProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final FeedRepository feedRepository;
    private final FeedRescheduleProcessor feedRescheduleProcessor;

    @Value("${batch.page.size:100}")
    private int pageSize;

    @Bean
    public Job rescheduleFeedsJob(JobRepository jobRepository, Step rescheduleFeedsStep) {
        return new JobBuilder("rescheduleFeedsJob", jobRepository)
                .start(rescheduleFeedsStep)
                .build();
    }

    @Bean
    public Step rescheduleFeedsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("rescheduleFeedsStep", jobRepository)
                .tasklet(rescheduleFeedsTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet rescheduleFeedsTasklet() {
        return (contribution, chunkContext) -> {
            log.info("Starting feed rescheduling job with page size: {}", pageSize);
            
            long totalFeedCount = feedRepository.count();
            log.info("Found {} total feeds to reschedule", totalFeedCount);
            
            if (totalFeedCount > 0) {
                int totalPages = (int) Math.ceil((double) totalFeedCount / pageSize);
                int processedFeeds = 0;
                
                for (int page = 0; page < totalPages; page++) {
                    Pageable pageable = PageRequest.of(page, pageSize);
                    var feedPage = feedRepository.findAll(pageable);
                    
                    log.info("Processing page {}/{} with {} feeds", 
                            page + 1, totalPages, feedPage.getNumberOfElements());
                    
                    feedPage.getContent().forEach(feed -> {
                        log.debug("Rescheduling feed: {} (ID: {})", feed.getTitle(), feed.getId());
                        feedRescheduleProcessor.process(feed);
                    });
                    
                    processedFeeds += feedPage.getNumberOfElements();
                    log.info("Processed {}/{} feeds", processedFeeds, totalFeedCount);
                }
                
                log.info("Successfully rescheduled {} feeds in {} pages", processedFeeds, totalPages);
            } else {
                log.info("No feeds found to reschedule");
            }
            
            return RepeatStatus.FINISHED;
        };
    }
}
