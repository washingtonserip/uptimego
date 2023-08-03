package io.uptimego.repository;

import io.uptimego.domain.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StatusRepositoryTest {

    @Autowired
    private StatusRepository statusRepository;

    @Test
    public void whenFindAll_thenReturnAllStatuses() {
        List<Status> statuses = statusRepository.findAll();
        assertThat(statuses).isNotNull();
    }
}