package io.uptimego.service;

import io.uptimego.model.UptimeConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UptimeConfigService {

    UptimeConfig create(UptimeConfig uptimeConfig);

    Optional<UptimeConfig> findById(Long id);

    List<UptimeConfig> findAll();

    Page<UptimeConfig> findAll(Pageable pageable);

    UptimeConfig update(UptimeConfig uptimeConfig);

    void delete(Long id);
}
