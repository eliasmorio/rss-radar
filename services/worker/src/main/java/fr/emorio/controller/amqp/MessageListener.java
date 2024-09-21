package fr.emorio.controller.amqp;

import fr.emorio.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {
    private static final String WORKER_QUEUE = "worker-queue";
    
    private final CrawlerService crawlerService;
    
    @RabbitListener(queues = WORKER_QUEUE)
    public void listen(String message)  {
        log.info("Received message: {}", message);
        try {
            crawlerService.crawl(Long.parseLong(message));
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
        }
    }
    
}
