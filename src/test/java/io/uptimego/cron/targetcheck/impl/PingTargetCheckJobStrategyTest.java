package io.uptimego.cron.targetcheck.impl;

import io.uptimego.EntityTestFactory;
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
public class PingTargetCheckJobStrategyTest {
    @Mock
    private NetworkService networkService;

    @InjectMocks
    private PingTargetCheckStrategy pingUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        uptimeConfig = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io", UptimeConfigType.PING);
    }

    @Test
    public void testCheckUptime_withReachableHost_shouldReturnUp() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(true);

        Pulse pulse = pingUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.UP, pulse.getStatus());
    }

    @Test
    public void testCheckUptime_withUnreachableHost_shouldReturnDown() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");
        InetAddress inetAddress = mock(InetAddress.class);

        when(networkService.getByName("host")).thenReturn(inetAddress);
        when(networkService.isReachable(inetAddress, 2000)).thenReturn(false);

        Pulse pulse = pingUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
    }

    @Test
    public void testCheckUptime_withException_shouldReturnDown() throws Exception {
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("host");

        when(networkService.getByName("host")).thenThrow(UnknownHostException.class);

        Pulse pulse = pingUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
    }
}
