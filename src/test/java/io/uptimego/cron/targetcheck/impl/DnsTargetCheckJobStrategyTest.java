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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DnsTargetCheckJobStrategyTest {

    @Mock
    private NetworkService networkService;

    @InjectMocks
    private DnsTargetCheckStrategy dnsUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        uptimeConfig = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io", UptimeConfigType.DNS);
    }

    @Test
    public void getPulse_Success() throws Exception {
        uptimeConfig.setUrl("https://uptimego.io");
        InetAddress localHostAddress = InetAddress.getLocalHost();
        when(networkService.getByName(uptimeConfig.getUrl())).thenReturn(localHostAddress);

        Pulse pulse = dnsUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.UP, pulse.getStatus());
        verify(networkService, times(1)).getByName(uptimeConfig.getUrl());
    }

    @Test
    public void getPulse_Failure() throws Exception {
        Exception error = new UnknownHostException("NUL character not allowed in hostname");
        when(networkService.getByName(uptimeConfig.getUrl())).thenThrow(error);

        Pulse pulse = dnsUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        verify(networkService, times(1)).getByName(uptimeConfig.getUrl());
    }
}
