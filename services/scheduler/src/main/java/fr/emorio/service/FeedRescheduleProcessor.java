package fr.emorio.service;

import fr.emorio.model.Feed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedRescheduleProcessor {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.crawl.queue.name}")
    private String crawlQueueName;

    public void process(Feed feed) {
        try {
            log.debug("Sending feed ID {} to queue {}", feed.getId(), crawlQueueName);
            rabbitTemplate.convertAndSend(crawlQueueName, feed.getId());
            log.debug("Successfully sent feed ID {} to queue", feed.getId());
        } catch (Exception e) {
            log.error("Failed to send feed ID {} to queue: {}", feed.getId(), e.getMessage(), e);
            throw new FeedRescheduleException("Failed to reschedule feed " + feed.getId(), e);
        }
    }
}
