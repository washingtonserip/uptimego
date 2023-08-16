package io.uptimego.cron.pulsecheck;

import static org.mockito.Mockito.*;

import java.util.Collections;

import io.uptimego.EntityTestFactory;
import io.uptimego.enums.PulseStatus;
import io.uptimego.model.Pulse;
import io.uptimego.model.Target;
import io.uptimego.model.User;
import io.uptimego.repository.AlertRepository;
import io.uptimego.repository.PulseRepository;
import io.uptimego.service.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class PulseCheckJobTest {

    @Mock
    private PulseRepository pulseRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private PulseCheckJob pulseCheckJob;

    @Test
    public void testExecuteNoPulses() throws Exception {
        when(pulseRepository.findByStatus(eq(PulseStatus.DOWN), any(Pageable.class))).thenReturn(Page.empty());

        pulseCheckJob.execute();

        verify(pulseRepository, times(1)).findByStatus(eq(PulseStatus.DOWN), any(Pageable.class));
        verify(alertService, never()).createNonDuplicatedAlert(any());
    }

    @Test
    public void testExecuteWithPulses() throws Exception {
        User user = EntityTestFactory.createUser();
        Target config = EntityTestFactory.createTarget(user, "https://uptimego.io");
        Pulse pulse = EntityTestFactory.createPulse(config, PulseStatus.UP, 50);
        Page<Pulse> page = new PageImpl<>(Collections.singletonList(pulse));
        when(pulseRepository.findByStatus(eq(PulseStatus.DOWN), any(Pageable.class))).thenReturn(page);

        pulseCheckJob.execute();

        verify(pulseRepository, times(1)).findByStatus(eq(PulseStatus.DOWN), any(Pageable.class));
        verify(alertService, times(1)).createNonDuplicatedAlert(any());
    }

    @Test
    public void testExecuteWithException() throws Exception {
        User user = EntityTestFactory.createUser();
        Target config = EntityTestFactory.createTarget(user, "https://uptimego.io");
        Pulse pulse = EntityTestFactory.createPulse(config, PulseStatus.UP, 50);
        Page<Pulse> page = new PageImpl<>(Collections.singletonList(pulse));
        when(pulseRepository.findByStatus(eq(PulseStatus.DOWN), any(Pageable.class))).thenReturn(page);
        doThrow(new RuntimeException("Test Exception")).when(alertService).createNonDuplicatedAlert(any());

        pulseCheckJob.execute();

        verify(pulseRepository, times(1)).findByStatus(eq(PulseStatus.DOWN), any(Pageable.class));
        verify(alertService, times(1)).createNonDuplicatedAlert(any());
    }
}
