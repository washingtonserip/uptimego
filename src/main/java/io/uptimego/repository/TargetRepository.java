package io.uptimego.repository;

import io.uptimego.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Integer> {
    // Métodos CRUD e de manipulação de banco de dados são gerados automaticamente
}
