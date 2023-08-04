package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.HeartbeatService;
import io.uptimego.service.UptimeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class HeartbeatProcessor {

    private final HeartbeatService heartbeatService;
    private final UptimeProcessor uptimeProcessor;
    private final ScheduledExecutorService executorService;
    private final UptimeService uptimeService;

    @Autowired
    public HeartbeatProcessor(HeartbeatService heartbeatService, UptimeProcessor uptimeProcessor, UptimeService uptimeService) {
        this.heartbeatService = heartbeatService;
        this.uptimeProcessor = uptimeProcessor;
        this.uptimeService = uptimeService;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startProcessing() {
        executorService.scheduleAtFixedRate(this::processHeartbeats, 0, 10, TimeUnit.MINUTES);
    }

    private void processHeartbeats() {
        List<Heartbeat> heartbeats = heartbeatService.getAllHeartbeats();
        for (int i = 0; i < heartbeats.size(); i += 100) {
            List<Heartbeat> batch = heartbeats.subList(i, Math.min(i + 100, heartbeats.size()));
            processBatch(batch);
        }
    }

    private void processBatch(List<Heartbeat> batch) {
        for (Heartbeat heartbeat : batch) {
            Uptime uptime = uptimeProcessor.processUptime(heartbeat);
            this.uptimeService.createUptime(uptime);
        }
    }
}
