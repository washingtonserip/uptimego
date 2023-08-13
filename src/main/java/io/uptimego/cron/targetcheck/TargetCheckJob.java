package io.uptimego.cron.targetcheck;

import io.uptimego.model.PlanSlug;
import io.uptimego.model.Pulse;
import io.uptimego.model.UptimeConfig;
import io.uptimego.repository.PulseRepository;
import io.uptimego.repository.UptimeConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TargetCheckJob {
    private static final Logger log = LoggerFactory.getLogger(TargetCheckScheduler.class);

    @Autowired
    private UptimeConfigRepository uptimeConfigRepository;

    @Autowired
    private TargetCheckProcessor targetCheckProcessor;

    @Autowired
    private PulseRepository pulseRepository;

    public void execute(PlanSlug planSlug) throws Exception {
        int pageSize = 10; // Adjust as needed
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<UptimeConfig> page;
        log.info("Executing TargetCheckJob");

        do {
            page = uptimeConfigRepository.findByPlanSlug(planSlug, pageable);
            if(!page.hasContent()) {
                break;
            }

            List<Pulse> pulses = targetCheckProcessor.process(page.getContent());
            if(pulses.isEmpty()) {
                break;
            }
            pulses.removeIf(Pulse::isEmpty);
            pulseRepository.saveAll(pulses);
            pageable = pageable.next();
        } while (page.hasNext());
    }
}

