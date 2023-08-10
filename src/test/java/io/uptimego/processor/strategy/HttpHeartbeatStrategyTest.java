package io.uptimego.processor.strategy;

import io.hypersistence.tsid.TSID;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigType;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpHeartbeatStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HttpHeartbeatStrategy httpUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(UUID.randomUUID());
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig.setUser(user);
        uptimeConfig.setType(UptimeConfigType.HTTP);
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

        assertEquals("up", heartbeat.getStatus());
        assertEquals(uptimeConfig.getId(), heartbeat.getUptimeConfig().getId());
        assertEquals(200, heartbeat.getDetails().getResponseCode());
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

        assertEquals("down", heartbeat.getStatus());
        assertEquals(uptimeConfig.getId(), heartbeat.getUptimeConfig().getId());
        assertEquals(404, heartbeat.getDetails().getResponseCode());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenExceptionOccurs() throws IOException {
        uptimeConfig.setUrl("http://invalidurl.com");
        Exception error = new IOException();
        when(httpClientService.executeGetRequest(anyString())).thenThrow(error);

        Heartbeat heartbeat = httpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals(uptimeConfig.getId(), heartbeat.getUptimeConfig().getId());
        assertEquals(error.getMessage(), heartbeat.getDetails().getStatusReason());
    }
}
