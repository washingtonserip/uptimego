package io.uptimego.service;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.repository.HeartbeatRepository;
import io.uptimego.repository.UptimeConfigRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class HeartbeatService {
    private HeartbeatRepository heartbeatRepository;

    public Heartbeat save(Heartbeat heartbeat) {
        return heartbeatRepository.save(heartbeat);
    }

}
