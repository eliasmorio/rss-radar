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

    private final CrawlerService crawlerService;
    private final ExplorerService explorerService;
    
    @RabbitListener(queues = "${rabbitmq.crawl.queue.name}", priority = "10")
    public void listenCrawl(String message)  {
        try {
            crawlerService.crawl(Long.parseLong(message));
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.explore.queue.name}", priority = "1")
    public void listenExplore(String message) {
        try {
            explorerService.explore(Long.parseLong(message));
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
        }
    }
    
}
