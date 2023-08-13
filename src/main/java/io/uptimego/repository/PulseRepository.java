package io.uptimego.repository;

import io.uptimego.model.Pulse;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PulseRepository extends PagingAndSortingRepository<Pulse, Long> {
}
