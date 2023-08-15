package io.uptimego.service.impl;

import io.uptimego.model.Alert;
import io.uptimego.enums.AlertStatus;
import io.uptimego.enums.AlertType;
import io.uptimego.model.Pulse;
import io.uptimego.repository.AlertRepository;
import io.uptimego.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;

    @Override
    public Alert createNonDuplicatedAlert(Pulse pulse) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        Alert existentAlert = alertRepository.findByPulse(pulse, threshold);
        if (existentAlert != null) {
            log.info("Alert already exists for alert {}", pulse.getId());
            return existentAlert;
        }

        return createAlert(pulse);
    }

    @Override
    public Alert createAlert(Pulse pulse) {
        Alert alert = new Alert();
        alert.setUser(pulse.getTarget().getUser());
        alert.setPulse(pulse);
        alert.setAlertType(AlertType.DOWNTIME);
        alert.setStatus(AlertStatus.CREATED);
        alertRepository.save(alert);
        return alert;
    }
}
