package io.uptimego.cron.targetcheck;

import io.uptimego.model.Pulse;
import io.uptimego.model.Target;
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
public class TargetCheckProcessor {

    private static final Logger log = LoggerFactory.getLogger(TargetCheckProcessor.class);

    @Autowired
    private TargetCheckStrategyHandler targetCheckStrategyHandler;

    public List<Pulse> process(List<Target> configs) throws Exception {
        List<CompletableFuture<Pulse>> futures = new ArrayList<>();
        log.info("Processing {} targets", configs.size());

        for (Target config : configs) {
            try {
                futures.add(processAsync(config));
            } catch (Exception e) {
                log.error("Error processing pulse for: {}", config.getId(), e);
            }
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Wait for all asynchronous tasks to complete
        CompletableFuture<List<Pulse>> allPulsesFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        return allPulsesFuture.join();
    }

    @Async
    public CompletableFuture<Pulse> processAsync(Target target) {
        log.info("Processing targets: {}", target);
        Pulse pulse = targetCheckStrategyHandler.execute(target);
        log.info("Successfully processed pulse: {}", pulse);

        return CompletableFuture.completedFuture(pulse);
    }
}
