package fr.emorio.service;

import fr.emorio.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final FeedRepository feedRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.crawl.queue.name}")
    private String CRAWL_QUEUE_NAME;

    @Scheduled(cron = "0 25 * * * *")
    public void schedule() {
        feedRepository.findAll()
                .forEach(feed -> {
                    log.info("Scheduling feed: {}", feed);
                    rabbitTemplate.convertAndSend(CRAWL_QUEUE_NAME, feed.getId());
                });
    }


}
