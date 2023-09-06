package io.uptimego.repository;

import io.uptimego.model.Pulse;
import io.uptimego.enums.PulseStatus;
import io.uptimego.model.Target;
import io.uptimego.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PulseRepository extends PagingAndSortingRepository<Pulse, Long> {
    Page<Pulse> findByStatus(PulseStatus pulseStatus, Pageable pageable);

    List<Pulse> findByUser(User user);
}
