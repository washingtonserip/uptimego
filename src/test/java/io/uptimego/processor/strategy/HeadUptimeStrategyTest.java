package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HeadUptimeStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HeadUptimeStrategy headUptimeStrategy;

    private Heartbeat heartbeat;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setUrl("http://example.com");
    }

    @Test
    public void checkUptime_Success() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(true);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Uptime uptime = headUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void checkUptime_Failure() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(false);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Uptime uptime = headUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void checkUptime_Exception() throws Exception {
        when(httpClientService.executeHeadRequest(any())).thenThrow(new IOException());

        Uptime uptime = headUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }
}
