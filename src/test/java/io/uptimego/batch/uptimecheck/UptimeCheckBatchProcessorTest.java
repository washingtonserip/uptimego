package io.uptimego.batch.uptimecheck;

import io.uptimego.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of());
        assertTrue(heartbeats.isEmpty());
    }

    @Test
    public void testProcessWithOneSuccessfulConfig() throws Exception {
        UptimeConfig config = new UptimeConfig(); // Assuming a default constructor
        Heartbeat expectedHeartbeat = new Heartbeat();

        when(uptimeCheckStrategyHandler.execute(config)).thenReturn(expectedHeartbeat);

        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of(config));

        assertEquals(1, heartbeats.size());
        assertEquals(expectedHeartbeat, heartbeats.get(0));
    }

    @Test
    public void testProcessWithOneFailedConfig() throws Exception {
        UptimeConfig config = new UptimeConfig();

        doThrow(new RuntimeException("Test exception")).when(uptimeCheckStrategyHandler).execute(config);

        assertEquals(0, uptimeCheckBatchProcessor.process(List.of(config)).size());
    }

    @Test
    public void testProcessWithMixedConfigs() throws Exception {
        UptimeConfig config1 = new UptimeConfig();
        UptimeConfig config2 = new UptimeConfig();
        Heartbeat expectedHeartbeat = new Heartbeat();

        when(uptimeCheckStrategyHandler.execute(config1)).thenReturn(expectedHeartbeat);
        when(uptimeCheckStrategyHandler.execute(config2)).thenThrow(new RuntimeException("Test exception"));

        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of(config1, config2));

        assertEquals(1, heartbeats.size());
        assertEquals(expectedHeartbeat, heartbeats.get(0));
    }

    @Test
    public void testAsyncProcessing() throws Exception {
        User user = new User();
        user.setId(1L);

        UptimeConfig config1 = new UptimeConfig();
        config1.setId(1L);
        config1.setUser(user);
        config1.setUrl("http://www.google.com");
        config1.setType(UptimeConfigType.HTTP);

        UptimeConfig config2 = new UptimeConfig();
        config2.setId(1L);
        config2.setUser(user);
        config2.setUrl("http://www.yahoo.com");
        config2.setType(UptimeConfigType.HTTP);

        Heartbeat heartbeat1 = new Heartbeat();
        heartbeat1.setId(1L);
        heartbeat1.setUptimeConfig(config1);
        heartbeat1.setStatus(HeartbeatStatus.DOWN);
        heartbeat1.setLatency(100);
        heartbeat1.setTimestamp(LocalDateTime.now());

        Heartbeat heartbeat2 = new Heartbeat();
        heartbeat2.setId(1L);
        heartbeat2.setUptimeConfig(config2);
        heartbeat2.setStatus(HeartbeatStatus.UP);
        heartbeat2.setLatency(50);
        heartbeat2.setTimestamp(LocalDateTime.now());

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return heartbeat1;
        }).when(uptimeCheckStrategyHandler).execute(eq(config1));

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return heartbeat2;
        }).when(uptimeCheckStrategyHandler).execute(eq(config2));

        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of(config1, config2));

        assertEquals(2, heartbeats.size());
        assertEquals(heartbeat1, heartbeats.get(0));
        assertEquals(heartbeat2, heartbeats.get(1));
    }
}
