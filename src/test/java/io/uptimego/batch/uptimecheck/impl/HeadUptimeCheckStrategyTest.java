package io.uptimego.batch.uptimecheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeadUptimeCheckStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HeadUptimeCheckStrategy headUptimeCheckStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        uptimeConfig = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io", UptimeConfigType.HEAD);
    }

    @Test
    public void getHeartbeat_Success() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(true);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Heartbeat heartbeat = headUptimeCheckStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.UP, heartbeat.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void getHeartbeat_Failure() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(false);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Heartbeat heartbeat = headUptimeCheckStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void getHeartbeat_Exception() throws Exception {
        Exception error = new IOException();
        when(httpClientService.executeHeadRequest(any())).thenThrow(error);

        Heartbeat heartbeat = headUptimeCheckStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }
}
