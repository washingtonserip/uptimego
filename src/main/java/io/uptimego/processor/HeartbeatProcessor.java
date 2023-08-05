package io.uptimego.processor;

import io.uptimego.service.HeartbeatService;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.UptimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatProcessor {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatProcessor.class);
    public static final int PAGE_SIZE = 100;

    private final HeartbeatService heartbeatService;
    private final UptimeService uptimeService;
    private final UptimeProcessor uptimeProcessor;

    public HeartbeatProcessor(HeartbeatService heartbeatService, UptimeService uptimeService, UptimeProcessor uptimeProcessor) {
        this.heartbeatService = heartbeatService;
        this.uptimeService = uptimeService;
        this.uptimeProcessor = uptimeProcessor;
    }

    @Scheduled(fixedRate = 600000)  // every 10 minutes
    public void processHeartbeats() {
        log.info("Heartbeat processing started");

        int pageNumber = 0;
        Pageable pageable;
        Page<Heartbeat> heartbeatPage;

        do {
            pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            heartbeatPage = getPageOfHeartbeats(pageable);

            log.info("Processing page number: {}", pageNumber);
            processHeartbeatsInPage(heartbeatPage);

            pageNumber++;
        } while (heartbeatPage.hasNext());

        log.info("Heartbeat processing completed");
    }

    private Page<Heartbeat> getPageOfHeartbeats(Pageable pageable) {
        return heartbeatService.findAll(pageable);
    }

    private void processHeartbeatsInPage(Page<Heartbeat> heartbeatPage) {
        heartbeatPage.getContent().forEach(this::processSingleHeartbeat);
    }

    private void processSingleHeartbeat(Heartbeat heartbeat) {
        try {
            Uptime uptime = uptimeProcessor.processUptime(heartbeat);
            uptimeService.save(uptime);
            log.info("Successfully processed heartbeat: {}", heartbeat.getId());
        } catch (Exception e) {
            handleProcessingError(heartbeat, e);
        }
    }

    private void handleProcessingError(Heartbeat heartbeat, Exception e) {
        log.error("Error processing heartbeat: {}", heartbeat.getId(), e);
    }
}
