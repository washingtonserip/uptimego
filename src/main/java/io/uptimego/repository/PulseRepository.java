package io.uptimego.repository;

import io.uptimego.model.Pulse;
import io.uptimego.enums.PulseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PulseRepository extends PagingAndSortingRepository<Pulse, Long> {
    Page<Pulse> findByStatus(PulseStatus pulseStatus, Pageable pageable);
}
