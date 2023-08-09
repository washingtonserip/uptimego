package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HeartbeatService;
import io.uptimego.service.UptimeConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatCronjob {

    public static final int PAGE_SIZE = 100;
    private static final Logger log = LoggerFactory.getLogger(HeartbeatCronjob.class);

    @Autowired
    private UptimeConfigService uptimeConfigService;
    @Autowired
    private HeartbeatService heartbeatService;
    @Autowired
    private HeartbeatProcessor heartbeatProcessor;

    @Scheduled(fixedRate = 600000)
    public void processHeartbeats() {
        log.info("Heartbeat processing started");

        int pageNumber = 0;
        Pageable pageable;
        Page<UptimeConfig> uptimeConfigPage;

        do {
            pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            uptimeConfigPage = getPageOfUptimeConfig(pageable);

            log.info("Processing page number: {}", pageNumber);
            processHeartbeatsInPage(uptimeConfigPage);

            pageNumber++;
        } while (uptimeConfigPage.hasNext());

        log.info("Heartbeat processing completed");
    }

    private Page<UptimeConfig> getPageOfUptimeConfig(Pageable pageable) {
        return uptimeConfigService.findAll(pageable);
    }

    private void processHeartbeatsInPage(Page<UptimeConfig> uptimeConfigPage) {
        uptimeConfigPage.getContent().forEach(this::processSingleUptimeConfig);
    }

    private void processSingleUptimeConfig(UptimeConfig uptimeConfig) {
        try {
            Heartbeat heartbeat = heartbeatProcessor.execute(uptimeConfig);
            this.heartbeatService.save(heartbeat);
            log.info("Successfully processed heartbeat: {}", heartbeat.getId());
        } catch (Exception e) {
            handleProcessingError(uptimeConfig, e);
        }
    }

    private void handleProcessingError(UptimeConfig uptimeConfig, Exception e) {
        log.error("Error processing heartbeat for: {}", uptimeConfig.getId(), e);
    }
}
