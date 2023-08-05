package io.uptimego.service;

import io.uptimego.model.Heartbeat;
import io.uptimego.repository.HeartbeatRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
