package io.uptimego.repository;

import io.uptimego.model.UptimeConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UptimeConfigRepository extends JpaRepository<UptimeConfig, Long> {
    Page<UptimeConfig> findAll(Pageable pageable);
}
