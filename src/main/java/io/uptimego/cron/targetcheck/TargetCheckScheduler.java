package io.uptimego.cron.targetcheck;

import io.uptimego.enums.PlanSlug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TargetCheckScheduler {
    private static final Logger log = LoggerFactory.getLogger(TargetCheckScheduler.class);

    @Autowired
    private TargetCheckJob targetCheckJob;

    @Scheduled(fixedRate = 30000)
    public void triggerTargetCheckForBasicUsers() {
        try {
            targetCheckJob.execute(PlanSlug.BASIC);
        } catch (Exception e) {
            log.error("Error executing TargetCheckJob", e);
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void triggerTargetCheckForProUsers() {
        try {
            targetCheckJob.execute(PlanSlug.PRO);
        } catch (Exception e) {
            log.error("Error executing TargetCheckJob", e);
            throw new RuntimeException(e);
        }
    }
}
