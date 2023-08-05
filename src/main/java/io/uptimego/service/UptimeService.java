package io.uptimego.service;

import io.uptimego.model.Uptime;
import io.uptimego.repository.UptimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UptimeService {
    private final UptimeRepository uptimeRepository;

    @Autowired
    public UptimeService (UptimeRepository uptimeRepository) {
        this.uptimeRepository = uptimeRepository;
    }

    public Uptime save(Uptime uptime) {
        return uptimeRepository.save(uptime);
    }

    // Repeat for other CRUD operations
}
