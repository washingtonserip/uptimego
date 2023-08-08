package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
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
public class DnsHeartbeatStrategyTest {

    @Mock
    private NetworkService networkService;

    @InjectMocks
    private DnsHeartbeatStrategy dnsUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(UUID.randomUUID());
        uptimeConfig.setUserId(UUID.randomUUID());
        uptimeConfig.setType(UptimeConfigType.DNS);
    }

    @Test
    public void getHeartbeat_Success() throws Exception {
        uptimeConfig.setUrl("http://uptimego.io");
        InetAddress localHostAddress = InetAddress.getLocalHost();
        when(networkService.getByName(uptimeConfig.getUrl())).thenReturn(localHostAddress);

        Heartbeat heartbeat = dnsUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("up", heartbeat.getStatus());
        assertEquals(localHostAddress.getHostAddress(), heartbeat.getDetails().getIpAddress());
        verify(networkService, times(1)).getByName(uptimeConfig.getUrl());
    }

    @Test
    public void getHeartbeat_Failure() throws Exception {
        Exception error = new UnknownHostException("NUL character not allowed in hostname");
        when(networkService.getByName(uptimeConfig.getUrl())).thenThrow(error);

        Heartbeat heartbeat = dnsUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals(error.getMessage(), heartbeat.getDetails().getStatusReason());
        verify(networkService, times(1)).getByName(uptimeConfig.getUrl());
    }
}
