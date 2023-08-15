package io.uptimego.cron.pulsecheck;

import io.uptimego.model.Alert;
import io.uptimego.model.Pulse;
import io.uptimego.service.AlertService;
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
public class PulseCheckProcessor {

    private static final Logger log = LoggerFactory.getLogger(PulseCheckProcessor.class);

    @Autowired
    private AlertService alertService;

    public List<Alert> process(List<Pulse> pulses) {
        List<CompletableFuture<Alert>> futures = new ArrayList<>();
        log.info("Processing {} pulses", pulses.size());

        for (Pulse pulse : pulses) {
            try {
                futures.add(processAsync(pulse));
            } catch (Exception e) {
                log.error("Error processing pulse for: {}", pulse.getId(), e);
            }
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Wait for all asynchronous tasks to complete
        CompletableFuture<List<Alert>> allPulsesFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        return allPulsesFuture.join();
    }

    @Async
    public CompletableFuture<Alert> processAsync(Pulse target) {
        log.info("Processing pulses: {}", target);
        Alert alert = alertService.createNonDuplicatedAlert(target);
        log.info("Successfully processed pulse: {}", alert);

        return CompletableFuture.completedFuture(alert);
    }
}
