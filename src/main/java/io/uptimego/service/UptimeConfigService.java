package io.uptimego.service;

import io.uptimego.model.UptimeConfig;
import io.uptimego.repository.UptimeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UptimeConfigService {

    UptimeConfig create(UptimeConfig uptimeConfig);

    Optional<UptimeConfig> findById(UUID id);

    List<UptimeConfig> findAll();

    Page<UptimeConfig> findAll(Pageable pageable);

    UptimeConfig update(UptimeConfig uptimeConfig);

    void delete(UUID id);
}
