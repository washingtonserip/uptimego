package io.uptimego.repository;

import io.uptimego.model.Heartbeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartbeatRepository extends PagingAndSortingRepository<Heartbeat, Long> {
    Page<Heartbeat> findAll(Pageable pageable);
}
