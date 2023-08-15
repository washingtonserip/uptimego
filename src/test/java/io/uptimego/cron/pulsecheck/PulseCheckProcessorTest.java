package io.uptimego.cron.pulsecheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.enums.PulseStatus;
import io.uptimego.model.*;
import io.uptimego.service.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PulseCheckProcessorTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private PulseCheckProcessor pulseCheckProcessor;

    @Test
    public void testProcess() throws Exception {
        // Arrange
        User user = EntityTestFactory.createUser();
        Target mockConfig1 = EntityTestFactory.createTarget(user, "https://uptimego.io");
        Target mockConfig2 = EntityTestFactory.createTarget(user, "https://wpires.com.br");
        Pulse mockPulse1 = EntityTestFactory.createPulse(mockConfig1, PulseStatus.DOWN, 100);
        Pulse mockPulse2 = EntityTestFactory.createPulse(mockConfig2, PulseStatus.UP, 50);
        Alert mockAlert1 = EntityTestFactory.buildAlert(mockPulse1);
        Alert mockAlert2 = EntityTestFactory.buildAlert(mockPulse2);

        when(alertService.createNonDuplicatedAlert(eq(mockPulse1))).thenReturn(mockAlert1);
        when(alertService.createNonDuplicatedAlert(eq(mockPulse2))).thenReturn(mockAlert2);

        // Act
        List<Alert> alerts = pulseCheckProcessor.process(Arrays.asList(mockPulse1, mockPulse2));

        // Assert
        verify(alertService).createNonDuplicatedAlert(eq(mockPulse1));
        verify(alertService).createNonDuplicatedAlert(eq(mockPulse2));
        assertEquals(2, alerts.size());
        assertEquals(mockAlert1, alerts.get(0));
        assertEquals(mockAlert2, alerts.get(1));
    }

    @Test
    public void testProcess_EmptyList() throws Exception {
        // Arrange
        List<Pulse> emptyList = Collections.emptyList();

        // Act
        List<Alert> alerts = pulseCheckProcessor.process(emptyList);

        // Assert
        verify(alertService, never()).createNonDuplicatedAlert(any(Pulse.class));
        assertTrue(alerts.isEmpty());
    }

    @Test
    public void testProcess_ErrorInProcessing() throws Exception {
        // Arrange
        Pulse pulse = new Pulse(); // Set properties as needed
        when(alertService.createNonDuplicatedAlert(eq(pulse))).thenThrow(new RuntimeException("Error"));

        // Act
        List<Alert> alerts = pulseCheckProcessor.process(Arrays.asList(pulse));

        // Assert
        // Note that the implementation currently logs the error but continues processing.
        // So in this test, we expect an empty result.
        assertTrue(alerts.isEmpty());
    }
}
