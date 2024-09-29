package fr.emorio.service;

import fr.emorio.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final FeedRepository feedRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0 * * * *")
    public void schedule() {
        feedRepository.findAll().forEach(feed -> rabbitTemplate.convertAndSend("crawl-queue", feed.getUrl()));
    }


}
