package io.uptimego.batch.uptimecheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.User;
import io.uptimego.service.HttpClientService;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpUptimeCheckStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HttpUptimeCheckStrategy httpUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        uptimeConfig = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
    }

    @Test
    public void shouldReturnUptimeAsUpWhenResponseIsSuccessful() throws IOException {
        uptimeConfig.setUrl("http://example.com");

        Response response = new Response.Builder()
                .code(200)
                .request(new Request.Builder().url("http://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Successful")
                .build();

        when(httpClientService.executeGetRequest(anyString())).thenReturn(response);

        Heartbeat heartbeat = httpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.UP, heartbeat.getStatus());
        assertEquals(uptimeConfig.getId(), heartbeat.getUptimeConfig().getId());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenResponseIsNotSuccessful() throws IOException {
        uptimeConfig.setUrl("http://example.com");

        Response response = new Response.Builder()
                .code(404)
                .request(new Request.Builder().url("http://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Not Found")
                .build();

        when(httpClientService.executeGetRequest(anyString())).thenReturn(response);

        Heartbeat heartbeat = httpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
        assertEquals(uptimeConfig.getId(), heartbeat.getUptimeConfig().getId());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenExceptionOccurs() throws IOException {
        uptimeConfig.setUrl("http://invalidurl.com");
        Exception error = new IOException();
        when(httpClientService.executeGetRequest(anyString())).thenThrow(error);

        Heartbeat heartbeat = httpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
        assertEquals(uptimeConfig.getId(), heartbeat.getUptimeConfig().getId());
    }
}
