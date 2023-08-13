package io.uptimego.batch.uptimecheck;

import io.uptimego.model.PlanSlug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UptimeCheckScheduler {
    private static final Logger log = LoggerFactory.getLogger(UptimeCheckScheduler.class);

    @Autowired
    private UptimeCheckJob uptimeCheckJob;

    @Scheduled(fixedRate = 30000)
    public void triggerUptimeCheck() {
        try {
            uptimeCheckJob.execute(PlanSlug.BASIC);
        } catch (Exception e) {
            log.info("Error executing UptimeCheckJob", e);
            throw new RuntimeException(e);
        }
    }
}

