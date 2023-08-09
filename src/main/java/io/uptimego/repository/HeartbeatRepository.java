package io.uptimego.repository;

import io.uptimego.model.Heartbeat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HeartbeatRepository extends PagingAndSortingRepository<Heartbeat, UUID> {
    Heartbeat save(Heartbeat heartbeat);
}
