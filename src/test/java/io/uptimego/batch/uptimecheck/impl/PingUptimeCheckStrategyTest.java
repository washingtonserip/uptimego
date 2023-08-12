package io.uptimego.batch.uptimecheck.impl;

import io.hypersistence.tsid.TSID;
import io.uptimego.model.*;
import io.uptimego.service.NetworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PingUptimeCheckStrategyTest {
    @Mock
    private NetworkService networkService;

    @InjectMocks
    private PingUptimeCheckStrategy pingUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig.setUser(user);
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

        assertEquals(HeartbeatStatus.UP, heartbeat.getStatus());
    }

    @Test
    public void testCheckUptime_withUnreachableHost_shouldReturnDown() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(false);

        Heartbeat heartbeat = pingUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
    }

    @Test
    public void testCheckUptime_withException_shouldReturnDown() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");

        when(networkService.getByName("host")).thenThrow(UnknownHostException.class);

        Heartbeat heartbeat = pingUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
    }
}
