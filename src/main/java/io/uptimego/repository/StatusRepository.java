package io.uptimego.repository;

import io.uptimego.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    // Métodos CRUD e de manipulação de banco de dados são gerados automaticamente
}
