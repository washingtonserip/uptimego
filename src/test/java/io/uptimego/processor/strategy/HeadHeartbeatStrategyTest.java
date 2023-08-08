package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigType;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeadHeartbeatStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HeadHeartbeatStrategy headUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(UUID.randomUUID());
        uptimeConfig.setUserId(UUID.randomUUID());
        uptimeConfig.setType(UptimeConfigType.HEAD);
        uptimeConfig.setUrl("http://uptimego.io");
    }

    @Test
    public void getHeartbeat_Success() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(true);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Heartbeat heartbeat = headUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("up", heartbeat.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void getHeartbeat_Failure() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(false);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Heartbeat heartbeat = headUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals("Response code is not 2xx", heartbeat.getDetails().getStatusReason());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void getHeartbeat_Exception() throws Exception {
        Exception error = new IOException();
        when(httpClientService.executeHeadRequest(any())).thenThrow(error);

        Heartbeat heartbeat = headUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals(error.getMessage(), heartbeat.getDetails().getStatusReason());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }
}
