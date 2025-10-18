package fr.emorio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job rescheduleFeedsJob;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting RSS Radar Scheduler Batch Job");
        
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            
            jobLauncher.run(rescheduleFeedsJob, jobParameters);
            log.info("RSS Radar Scheduler Batch Job completed successfully");
        } catch (Exception e) {
            log.error("RSS Radar Scheduler Batch Job failed", e);
            throw e;
        }
    }
}
