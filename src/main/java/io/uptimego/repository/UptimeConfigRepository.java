package io.uptimego.repository;

import io.uptimego.model.PlanSlug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import io.uptimego.model.UptimeConfig;

public interface UptimeConfigRepository extends JpaRepository<UptimeConfig, Long> {
    Page<UptimeConfig> findAll(Pageable pageable);

    @Query("SELECT uc FROM UptimeConfig uc " +
            "JOIN uc.user u " +
            "JOIN u.subscription s " +
            "JOIN s.plan p " +
            "WHERE p.slug = :planSlug")
    Page<UptimeConfig> findByPlanSlug(PlanSlug planSlug, Pageable pageable);
}
