package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;
import io.uptimego.service.NetworkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PingUptimeStrategyTest {
    @Mock
    private NetworkService networkService;

    @InjectMocks
    private PingUptimeStrategy pingUptimeStrategy;

    @Test
    public void testCheckUptime_withReachableHost_shouldReturnUp() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(true);

        Uptime uptime = pingUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
    }

    @Test
    public void testCheckUptime_withUnreachableHost_shouldReturnDown() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(false);

        Uptime uptime = pingUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
    }

    @Test
    public void testCheckUptime_withException_shouldReturnDown() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("host");

        when(networkService.getByName("host")).thenThrow(UnknownHostException.class);

        Uptime uptime = pingUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
    }
}
