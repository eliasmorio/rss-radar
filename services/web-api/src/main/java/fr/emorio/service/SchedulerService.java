package fr.emorio.service;

import fr.emorio.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final FeedRepository feedRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0 * * * *")
    public void schedule() {
        feedRepository.findAll()
                .forEach(feed -> {
                    log.info("Scheduling feed: {}", feed);
                    rabbitTemplate.convertAndSend("crawl-queue", feed.getId());
                });
    }


}
