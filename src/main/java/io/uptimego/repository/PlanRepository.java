package io.uptimego.repository;

import io.uptimego.model.Plan;
import io.uptimego.model.PlanSlug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    Plan findBySlug(PlanSlug slug);
}
