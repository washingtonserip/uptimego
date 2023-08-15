package io.uptimego.repository;

import io.uptimego.model.Alert;
import io.uptimego.model.Pulse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Query("SELECT a FROM Alert a WHERE a.pulse = :pulse AND a.createdAt >= (NOW() - INTERVAL '5 MINUTE')")
    Alert findByPulse(Pulse pulse);
}
