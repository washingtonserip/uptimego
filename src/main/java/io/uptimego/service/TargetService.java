package io.uptimego.service;

import io.uptimego.model.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TargetService {

    Target create(Target target);

    Optional<Target> findById(Long id);

    List<Target> findAll();

    Page<Target> findAll(Pageable pageable);

    Target update(Target target);

    void delete(Long id);
}
