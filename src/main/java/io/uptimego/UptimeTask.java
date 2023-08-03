package io.uptimego;

import io.uptimego.domain.Status;
import io.uptimego.domain.Target
import io.uptimego.repository.StatusRepository;
import io.uptimego.repository.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.logging.Logger;

@Component
public class UptimeTask {

    private static final Logger LOGGER = Logger.getLogger(UptimeTask.class.getName());

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UptimeCheck uptimeCheck;

    @Scheduled(fixedRate = 600000) // 10 minutes
    public void checkUptime() {
        try {
            List<Target> targets = targetRepository.getTargetsBatch(100);
            for(Target target : targets) {
                try {
                    Status status = uptimeCheck.checkTarget(target.getId(), target.getUrl());
                    statusRepository.saveStatus(status);
                } catch (Exception e) {
                    LOGGER.severe("Failed to save status for target " + target.getUrl() + " due to: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to get targets from database due to: " + e.getMessage());
        }
    }
}
