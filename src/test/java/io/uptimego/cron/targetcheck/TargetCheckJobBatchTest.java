package io.uptimego.cron.targetcheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.enums.PulseStatus;
import io.uptimego.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TargetCheckJobBatchTest {

    @Mock
    private TargetCheckStrategyHandler targetCheckStrategyHandler;

    @InjectMocks
    private TargetCheckProcessor targetCheckProcessor;

    @Test
    public void testProcessWithEmptyConfigs() throws Exception {
        List<Pulse> pulses = targetCheckProcessor.process(List.of());
        assertTrue(pulses.isEmpty());
    }

    @Test
    public void testProcessWithOneSuccessfulConfig() throws Exception {
        User user = EntityTestFactory.buildUser();
        Target config = EntityTestFactory.buildTarget(user, "https://uptimego.io");
        Pulse expectedPulse = EntityTestFactory.buildPulse(config, PulseStatus.DOWN, 100);

        when(targetCheckStrategyHandler.execute(config)).thenReturn(expectedPulse);

        List<Pulse> pulses = targetCheckProcessor.process(List.of(config));

        assertEquals(1, pulses.size());
        assertEquals(expectedPulse, pulses.get(0));
    }

    @Test
    public void testProcessWithOneFailedConfig() throws Exception {
        Target config = new Target();

        doThrow(new RuntimeException("Test exception")).when(targetCheckStrategyHandler).execute(config);

        assertEquals(0, targetCheckProcessor.process(List.of(config)).size());
    }

    @Test
    public void testProcessWithMixedConfigs() throws Exception {
        User user = EntityTestFactory.buildUser();
        Target mockConfig1 = EntityTestFactory.buildTarget(user, "https://uptimego.io");
        Target mockConfig2 = EntityTestFactory.buildTarget(user, "https://wpires.com.br");
        Pulse expectedPulse = EntityTestFactory.buildPulse(mockConfig1, PulseStatus.DOWN, 100);

        when(targetCheckStrategyHandler.execute(mockConfig1)).thenReturn(expectedPulse);
        when(targetCheckStrategyHandler.execute(mockConfig2)).thenThrow(new RuntimeException("Test exception"));

        List<Pulse> pulses = targetCheckProcessor.process(List.of(mockConfig1, mockConfig2));

        assertEquals(1, pulses.size());
        assertEquals(expectedPulse, pulses.get(0));
    }

    @Test
    public void testAsyncProcessing() throws Exception {
        User user = EntityTestFactory.buildUser();
        Target mockConfig1 = EntityTestFactory.buildTarget(user, "https://uptimego.io");
        Target mockConfig2 = EntityTestFactory.buildTarget(user, "https://wpires.com.br");
        Pulse mockPulse1 = EntityTestFactory.buildPulse(mockConfig1, PulseStatus.DOWN, 100);
        Pulse mockPulse2 = EntityTestFactory.buildPulse(mockConfig2, PulseStatus.UP, 50);

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return mockPulse1;
        }).when(targetCheckStrategyHandler).execute(eq(mockConfig1));

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return mockPulse2;
        }).when(targetCheckStrategyHandler).execute(eq(mockConfig2));

        List<Pulse> pulses = targetCheckProcessor.process(List.of(mockConfig1, mockConfig2));

        assertEquals(2, pulses.size());
        assertEquals(mockPulse1, pulses.get(0));
        assertEquals(mockPulse2, pulses.get(1));
    }
}
