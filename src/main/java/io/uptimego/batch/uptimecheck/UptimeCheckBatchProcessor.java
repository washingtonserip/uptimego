package io.uptimego.batch.uptimecheck;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@EnableAsync(proxyTargetClass = true)
public class UptimeCheckBatchProcessor {

    private static final Logger log = LoggerFactory.getLogger(UptimeCheckBatchProcessor.class);

    @Autowired
    private UptimeCheckStrategyHandler uptimeCheckStrategyHandler;

    public List<Heartbeat> process(List<UptimeConfig> configs) throws Exception {
        List<CompletableFuture<Heartbeat>> futures = new ArrayList<>();
        log.info("Processing {} UptimeConfigs", configs.size());

        for (UptimeConfig config : configs) {
            try {
                futures.add(processAsync(config));
            } catch (Exception e) {
                log.error("Error processing heartbeat for: {}", config.getId(), e);
            }
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Wait for all asynchronous tasks to complete
        CompletableFuture<List<Heartbeat>> allHeartbeatsFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        return allHeartbeatsFuture.join();
    }

    @Async
    public CompletableFuture<Heartbeat> processAsync(UptimeConfig uptimeConfig) {
        log.info("Processing UptimeConfig: {}", uptimeConfig);
        Heartbeat heartbeat = uptimeCheckStrategyHandler.execute(uptimeConfig);
        log.info("Successfully processed heartbeat: {}", heartbeat);

        return CompletableFuture.completedFuture(heartbeat);
    }
}
