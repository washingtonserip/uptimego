package io.uptimego.service;

import io.uptimego.model.Heartbeat;
import io.uptimego.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatService {
    @Autowired
    private HeartbeatRepository heartbeatRepository;

    public Heartbeat save(Heartbeat heartbeat) {
        return heartbeatRepository.save(heartbeat);
    }

}
