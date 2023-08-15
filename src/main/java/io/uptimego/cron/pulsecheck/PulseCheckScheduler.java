package io.uptimego.cron.pulsecheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PulseCheckScheduler {
    private static final Logger log = LoggerFactory.getLogger(PulseCheckScheduler.class);

    @Autowired
    private PulseCheckJob targetCheckJob;

    @Scheduled(fixedRate = 30000)
    public void triggerPulseCheck() {
        try {
            targetCheckJob.execute();
        } catch (Exception e) {
            log.error("Error executing PulseCheckJob", e);
            throw new RuntimeException(e);
        }
    }
}
