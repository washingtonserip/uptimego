package io.uptimego.repository;

import io.uptimego.model.Uptime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UptimeRepository extends JpaRepository<Uptime, Long> {
    // Additional methods if required
}
