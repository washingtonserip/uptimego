package io.uptimego.repository;

import io.uptimego.domain.Target;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TargetRepositoryTest {

    @Autowired
    private TargetRepository targetRepository;

    @Test
    public void whenFindAll_thenReturnAllTargets() {
        List<Target> targets = targetRepository.findAll();
        assertThat(targets).isNotNull();
    }
}
