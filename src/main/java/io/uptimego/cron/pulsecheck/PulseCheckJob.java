package io.uptimego.cron.pulsecheck;

import io.uptimego.model.Alert;
import io.uptimego.model.Pulse;
import io.uptimego.enums.PulseStatus;
import io.uptimego.repository.AlertRepository;
import io.uptimego.repository.PulseRepository;
import io.uptimego.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PulseCheckJob {
    private static final Logger log = LoggerFactory.getLogger(PulseCheckJob.class);

    @Autowired
    private PulseRepository pulseRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertService alertService;

    public void execute() throws Exception {
        int pageSize = 10; // Adjust as needed
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<Pulse> page;
        log.info("Executing PulseCheckJob");

        do {
            page = pulseRepository.findByStatus(PulseStatus.DOWN, pageable);
            if(!page.hasContent()) {
                break;
            }

            process(page.getContent());
            pageable = pageable.next();
        } while (page.hasNext());
    }

    public void process(List<Pulse> pulses) {
        for (Pulse pulse : pulses) {
            try {
                alertService.createNonDuplicatedAlert(pulse);
            } catch (Exception e) {
                log.error("Error processing pulse for: {}", pulse.getId(), e);
            }
        }
    }
}
