package io.uptimego.service;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.repository.HeartbeatRepository;
import io.uptimego.repository.UptimeConfigRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UptimeConfigService {
    @Autowired private UptimeConfigRepository uptimeConfigRepository;

    public Page<UptimeConfig> findAll(Pageable pageable) {
        return uptimeConfigRepository.findAll(pageable);
    }

    public UptimeConfig save(UptimeConfig uptimeConfig) {
        return uptimeConfigRepository.save(uptimeConfig);
    }

    public Optional<UptimeConfig> findById(UUID id) {
        return uptimeConfigRepository.findById(id);
    }

    public void delete(UptimeConfig existingHeartbeat) {
        uptimeConfigRepository.delete(existingHeartbeat);
    }
}
