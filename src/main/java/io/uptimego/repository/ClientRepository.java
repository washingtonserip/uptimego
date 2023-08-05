package io.uptimego.repository;

import io.uptimego.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // Additional methods if required
}
