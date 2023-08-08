package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.model.UptimeConfigType;
import io.uptimego.service.NetworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PingHeartbeatStrategyTest {
    @Mock
    private NetworkService networkService;

    @InjectMocks
    private PingHeartbeatStrategy pingUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(UUID.randomUUID());
        uptimeConfig.setUserId(UUID.randomUUID());
        uptimeConfig.setType(UptimeConfigType.PING);
    }

    @Test
    public void testCheckUptime_withReachableHost_shouldReturnUp() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(true);

        Heartbeat heartbeat = pingUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("up", heartbeat.getStatus());
    }

    @Test
    public void testCheckUptime_withUnreachableHost_shouldReturnDown() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(false);

        Heartbeat heartbeat = pingUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
    }

    @Test
    public void testCheckUptime_withException_shouldReturnDown() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");

        when(networkService.getByName("host")).thenThrow(UnknownHostException.class);

        Heartbeat heartbeat = pingUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
    }
}
