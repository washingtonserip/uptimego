package io.uptimego;

import io.uptimego.domain.Status;
import io.uptimego.domain.Target;
import io.uptimego.service.UptimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class UptimeTask {

    private final UptimeService uptimeService;
    private final UptimeCheck uptimeCheck;

    @Autowired
    public UptimeTask(UptimeService uptimeService, UptimeCheck uptimeCheck) {
        this.uptimeService = uptimeService;
        this.uptimeCheck = uptimeCheck;
    }

    @Scheduled(fixedRate = 600000)
    public void checkUptime() {
        List<Target> targets = uptimeService.getAllTargets();
        Flux.fromIterable(targets)
                .flatMap(uptimeCheck::checkTarget)
                .subscribe(result -> {
                    Status status = new Status();
                    status.setTargetId(result.getTargetId());
                    status.setUp(result.getStatus() == 200);
                    status.setResponse(result.getBody());
                    uptimeService.saveStatus(status);
                });
    }
}
