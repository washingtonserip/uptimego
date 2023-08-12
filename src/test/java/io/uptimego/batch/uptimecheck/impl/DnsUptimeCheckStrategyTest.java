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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DnsUptimeCheckStrategyTest {

    @Mock
    private NetworkService networkService;

    @InjectMocks
    private DnsUptimeCheckStrategy dnsUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig.setUser(user);
        uptimeConfig.setType(UptimeConfigType.DNS);
    }

    @Test
    public void getHeartbeat_Success() throws Exception {
        uptimeConfig.setUrl("http://uptimego.io");
        InetAddress localHostAddress = InetAddress.getLocalHost();
        when(networkService.getByName(uptimeConfig.getUrl())).thenReturn(localHostAddress);

        Heartbeat heartbeat = dnsUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.UP, heartbeat.getStatus());
        verify(networkService, times(1)).getByName(uptimeConfig.getUrl());
    }

    @Test
    public void getHeartbeat_Failure() throws Exception {
        Exception error = new UnknownHostException("NUL character not allowed in hostname");
        when(networkService.getByName(uptimeConfig.getUrl())).thenThrow(error);

        Heartbeat heartbeat = dnsUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
        verify(networkService, times(1)).getByName(uptimeConfig.getUrl());
    }
}
