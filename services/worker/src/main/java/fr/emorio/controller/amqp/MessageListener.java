package fr.emorio.controller.amqp;

import fr.emorio.service.CrawlerService;
import fr.emorio.service.ExplorerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {
    private static final String WORKER_QUEUE = "crawl-queue";
    private static final String EXPLORE_QUEUE = "explore-queue";

    private final CrawlerService crawlerService;
    private final ExplorerService explorerService;
    
    @RabbitListener(queues = WORKER_QUEUE, priority = "10")
    public void listenCrawl(String message)  {
        try {
            crawlerService.crawl(Long.parseLong(message));
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
        }
    }

    @RabbitListener(queues = EXPLORE_QUEUE, priority = "1")
    public void listenExplore(String message) {
        try {
            explorerService.explore(Long.parseLong(message));
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
        }
    }
    
}
