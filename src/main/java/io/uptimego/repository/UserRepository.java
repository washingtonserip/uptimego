package io.uptimego.repository;

import io.uptimego.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional methods if required
}
