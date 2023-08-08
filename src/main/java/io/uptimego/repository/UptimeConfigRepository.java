package io.uptimego.repository;

import io.uptimego.model.UptimeConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UptimeConfigRepository extends JpaRepository<UptimeConfig, UUID> {
    Page<UptimeConfig> findAll(Pageable pageable);
}
