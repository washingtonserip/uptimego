package io.uptimego.batch.uptimecheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UptimeCheckBatchProcessorTest {

    @Autowired
    private UptimeCheckBatchProcessor uptimeCheckBatchProcessor;

    @MockBean
    private UptimeCheckStrategyHandler uptimeCheckStrategyHandler;

    @Test
    public void testProcessWithEmptyConfigs() throws Exception {
        List<Pulse> pulses = uptimeCheckBatchProcessor.process(List.of());
        assertTrue(pulses.isEmpty());
    }

    @Test
    public void testProcessWithOneSuccessfulConfig() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig config = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        Pulse expectedPulse = EntityTestFactory.createPulse(config, PulseStatus.DOWN, 100);

        when(uptimeCheckStrategyHandler.execute(config)).thenReturn(expectedPulse);

        List<Pulse> pulses = uptimeCheckBatchProcessor.process(List.of(config));

        assertEquals(1, pulses.size());
        assertEquals(expectedPulse, pulses.get(0));
    }

    @Test
    public void testProcessWithOneFailedConfig() throws Exception {
        UptimeConfig config = new UptimeConfig();

        doThrow(new RuntimeException("Test exception")).when(uptimeCheckStrategyHandler).execute(config);

        assertEquals(0, uptimeCheckBatchProcessor.process(List.of(config)).size());
    }

    @Test
    public void testProcessWithMixedConfigs() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Pulse expectedPulse = EntityTestFactory.createPulse(mockConfig1, PulseStatus.DOWN, 100);

        when(uptimeCheckStrategyHandler.execute(mockConfig1)).thenReturn(expectedPulse);
        when(uptimeCheckStrategyHandler.execute(mockConfig2)).thenThrow(new RuntimeException("Test exception"));

        List<Pulse> pulses = uptimeCheckBatchProcessor.process(List.of(mockConfig1, mockConfig2));

        assertEquals(1, pulses.size());
        assertEquals(expectedPulse, pulses.get(0));
    }

    @Test
    public void testAsyncProcessing() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Pulse mockPulse1 = EntityTestFactory.createPulse(mockConfig1, PulseStatus.DOWN, 100);
        Pulse mockPulse2 = EntityTestFactory.createPulse(mockConfig2, PulseStatus.UP, 50);

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return mockPulse1;
        }).when(uptimeCheckStrategyHandler).execute(eq(mockConfig1));

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return mockPulse2;
        }).when(uptimeCheckStrategyHandler).execute(eq(mockConfig2));

        List<Pulse> pulses = uptimeCheckBatchProcessor.process(List.of(mockConfig1, mockConfig2));

        assertEquals(2, pulses.size());
        assertEquals(mockPulse1, pulses.get(0));
        assertEquals(mockPulse2, pulses.get(1));
    }
}
