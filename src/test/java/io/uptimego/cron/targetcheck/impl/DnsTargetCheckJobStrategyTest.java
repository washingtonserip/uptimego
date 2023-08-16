package io.uptimego.cron.targetcheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.enums.PulseStatus;
import io.uptimego.enums.TargetType;
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

    private Target target;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.buildUser();
        target = EntityTestFactory.buildTarget(user, "https://uptimego.io", TargetType.DNS);
    }

    @Test
    public void getPulse_Success() throws Exception {
        target.setUrl("https://uptimego.io");
        InetAddress localHostAddress = InetAddress.getLocalHost();
        when(networkService.getByName(target.getUrl())).thenReturn(localHostAddress);

        Pulse pulse = dnsUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.UP, pulse.getStatus());
        verify(networkService, times(1)).getByName(target.getUrl());
    }

    @Test
    public void getPulse_Failure() throws Exception {
        Exception error = new UnknownHostException("NUL character not allowed in hostname");
        when(networkService.getByName(target.getUrl())).thenThrow(error);

        Pulse pulse = dnsUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        verify(networkService, times(1)).getByName(target.getUrl());
    }
}
