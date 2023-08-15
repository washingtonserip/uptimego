package io.uptimego.cron.pulsecheck;

import io.uptimego.model.Alert;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.repository.AlertRepository;
import io.uptimego.repository.PulseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PulseCheckJobTest {

    @Mock
    private PulseCheckProcessor pulseCheckProcessor;

    @Mock
    private PulseRepository pulseRepository;

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private PulseCheckJob pulseCheckJob;

    @Test
    public void testExecute() throws Exception {
        // Arrange
        Pulse pulse = new Pulse(); // Set properties as needed
        Page<Pulse> page = new PageImpl<>(Arrays.asList(pulse));
        Alert alert = new Alert(); // Set properties as needed

        when(pulseRepository.findByStatus(eq(PulseStatus.DOWN), any(Pageable.class))).thenReturn(page);
        when(pulseCheckProcessor.process(anyList())).thenReturn(Arrays.asList(alert));

        // Act
        pulseCheckJob.execute();

        // Assert
        verify(pulseCheckProcessor).process(eq(Arrays.asList(pulse)));
        verify(alertRepository).saveAll(eq(Arrays.asList(alert)));
    }

    @Test
    public void testExecute_NoPulses() throws Exception {
        // Arrange
        Page<Pulse> page = new PageImpl<>(Collections.emptyList());

        when(pulseRepository.findByStatus(eq(PulseStatus.DOWN), any(PageRequest.class))).thenReturn(page);

        // Act
        pulseCheckJob.execute();

        // Assert
        verify(pulseCheckProcessor, never()).process(anyList());
        verify(alertRepository, never()).saveAll(anyList());
    }
}
