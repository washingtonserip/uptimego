package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatType;
import io.uptimego.model.Uptime;
import io.uptimego.processor.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UptimeProcessorTest {

    private UptimeProcessor uptimeProcessor;

    @BeforeEach
    public void setUp() {
        uptimeProcessor = new UptimeProcessor();
    }

    @Test
    public void processUptime_givenUnsupportedHeartbeatType_thenThrowException() {
        // Given
        Heartbeat heartbeat = mock(Heartbeat.class);
        when(heartbeat.getType()).thenReturn(HeartbeatType.UNKNOWN);

        // When
        Executable executable = () -> uptimeProcessor.processUptime(heartbeat);

        // Then
        assertThrows(IllegalArgumentException.class, executable, "Unsupported heartbeat type: UNKNOWN");
    }

    @Disabled
    @Test
    public void processUptime_givenSupportedHeartbeatType_thenProcessUptime() {
        // Given
        Heartbeat heartbeat = mock(Heartbeat.class);
        when(heartbeat.getType()).thenReturn(HeartbeatType.HTTP);
        when(heartbeat.getId()).thenReturn(1L);

        // HTTP strategy should be mocked for a more realistic test.
        Uptime uptimeExpected = new Uptime();
        HttpUptimeStrategy httpUptimeStrategy = Mockito.spy(new HttpUptimeStrategy());
        when(httpUptimeStrategy.checkUptime(any(Heartbeat.class))).thenReturn(uptimeExpected);

        // When
        Uptime uptime = uptimeProcessor.processUptime(heartbeat);

        // Then
        assertNotNull(uptime);
        assertEquals(uptimeExpected, uptime);
    }
}
