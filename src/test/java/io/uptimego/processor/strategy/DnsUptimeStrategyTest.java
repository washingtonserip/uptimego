package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
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
public class DnsUptimeStrategyTest {

    @Mock
    private NetworkService networkService;

    @InjectMocks
    private DnsUptimeStrategy dnsUptimeStrategy;

    private Heartbeat heartbeat;

    @BeforeEach
    public void setUp() {
        heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setUrl("http://example.com");
    }

    @Test
    public void checkUptime_Success() throws Exception {
        when(networkService.getByName(heartbeat.getUrl())).thenReturn(InetAddress.getLocalHost());

        Uptime uptime = dnsUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
        verify(networkService, times(1)).getByName(heartbeat.getUrl());
    }

    @Test
    public void checkUptime_Failure() throws Exception {
        when(networkService.getByName(heartbeat.getUrl())).thenThrow(new UnknownHostException());

        Uptime uptime = dnsUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        verify(networkService, times(1)).getByName(heartbeat.getUrl());
    }
}
