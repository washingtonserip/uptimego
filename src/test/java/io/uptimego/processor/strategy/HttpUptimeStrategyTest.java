package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatType;
import io.uptimego.model.Uptime;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HttpUptimeStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HttpUptimeStrategy httpUptimeStrategy;

    @BeforeEach
    public void setUp() {
        httpUptimeStrategy = new HttpUptimeStrategy(httpClientService);
    }

    @Test
    public void shouldReturnUptimeAsUpWhenResponseIsSuccessful() throws IOException {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setUrl("http://example.com");
        heartbeat.setType(HeartbeatType.HTTP);

        Response response = new Response.Builder()
                .code(200)
                .request(new Request.Builder().url("http://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Successful")
                .build();

        when(httpClientService.executeGetRequest(anyString())).thenReturn(response);

        Uptime uptime = httpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
        assertEquals(1L, uptime.getHeartbeatId());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenResponseIsNotSuccessful() throws IOException {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setUrl("http://example.com");
        heartbeat.setType(HeartbeatType.HTTP);

        Response response = new Response.Builder()
                .code(404)
                .request(new Request.Builder().url("http://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Not Found")
                .build();

        when(httpClientService.executeGetRequest(anyString())).thenReturn(response);

        Uptime uptime = httpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        assertEquals(1L, uptime.getHeartbeatId());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenExceptionOccurs() throws IOException {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setUrl("http://invalidurl.com");
        heartbeat.setType(HeartbeatType.HTTP);

        when(httpClientService.executeGetRequest(anyString())).thenThrow(IOException.class);

        Uptime uptime = httpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        assertEquals(1L, uptime.getHeartbeatId());
    }
}
