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
        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of());
        assertTrue(heartbeats.isEmpty());
    }

    @Test
    public void testProcessWithOneSuccessfulConfig() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig config = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        Heartbeat expectedHeartbeat = EntityTestFactory.createHeartbeat(config, HeartbeatStatus.DOWN, 100);

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
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Heartbeat expectedHeartbeat = EntityTestFactory.createHeartbeat(mockConfig1, HeartbeatStatus.DOWN, 100);

        when(uptimeCheckStrategyHandler.execute(mockConfig1)).thenReturn(expectedHeartbeat);
        when(uptimeCheckStrategyHandler.execute(mockConfig2)).thenThrow(new RuntimeException("Test exception"));

        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of(mockConfig1, mockConfig2));

        assertEquals(1, heartbeats.size());
        assertEquals(expectedHeartbeat, heartbeats.get(0));
    }

    @Test
    public void testAsyncProcessing() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Heartbeat mockHeartbeat1 = EntityTestFactory.createHeartbeat(mockConfig1, HeartbeatStatus.DOWN, 100);
        Heartbeat mockHeartbeat2 = EntityTestFactory.createHeartbeat(mockConfig2, HeartbeatStatus.UP, 50);

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return mockHeartbeat1;
        }).when(uptimeCheckStrategyHandler).execute(eq(mockConfig1));

        doAnswer(invocation -> {
            Thread.sleep(1000);
            return mockHeartbeat2;
        }).when(uptimeCheckStrategyHandler).execute(eq(mockConfig2));

        List<Heartbeat> heartbeats = uptimeCheckBatchProcessor.process(List.of(mockConfig1, mockConfig2));

        assertEquals(2, heartbeats.size());
        assertEquals(mockHeartbeat1, heartbeats.get(0));
        assertEquals(mockHeartbeat2, heartbeats.get(1));
    }
}
