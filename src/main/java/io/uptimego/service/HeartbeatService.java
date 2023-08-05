package io.uptimego.service;

import io.uptimego.model.Heartbeat;
import io.uptimego.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HeartbeatService {
    private final HeartbeatRepository heartbeatRepository;

    @Autowired
    public HeartbeatService(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    public Page<Heartbeat> findAll(Pageable pageable) {
        return heartbeatRepository.findAll(pageable);
    }

    public Heartbeat save(Heartbeat heartbeat) {
        return heartbeatRepository.save(heartbeat);
    }

    public Optional<Heartbeat> findById(Long id) {
        return heartbeatRepository.findById(id);
    }

    public void delete(Heartbeat existingHeartbeat) {
        heartbeatRepository.delete(existingHeartbeat);
    }
}
