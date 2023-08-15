package io.uptimego.repository;

import io.uptimego.model.PlanSlug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import io.uptimego.model.Target;
import org.springframework.data.repository.query.Param;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Page<Target> findAll(Pageable pageable);

    @Query("SELECT uc FROM Target uc " +
            "JOIN uc.user u " +
            "JOIN u.subscription s " +
            "JOIN s.plan p " +
            "WHERE p.slug = :slug")
    Page<Target> findByPlanSlug(@Param("slug") PlanSlug planSlug, Pageable pageable);
}
