package io.uptimego.cron.targetcheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.model.Target;
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
public class HttpTargetCheckJobStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HttpTargetCheckStrategy httpUptimeStrategy;

    private Target target;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        target = EntityTestFactory.createTarget(user, "https://uptimego.io");
    }

    @Test
    public void shouldReturnUptimeAsUpWhenResponseIsSuccessful() throws IOException {
        target.setUrl("https://example.com");

        Response response = new Response.Builder()
                .code(200)
                .request(new Request.Builder().url("https://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Successful")
                .build();

        when(httpClientService.executeGetRequest(anyString())).thenReturn(response);

        Pulse pulse = httpUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.UP, pulse.getStatus());
        assertEquals(target.getId(), pulse.getTarget().getId());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenResponseIsNotSuccessful() throws IOException {
        target.setUrl("https://example.com");

        Response response = new Response.Builder()
                .code(404)
                .request(new Request.Builder().url("https://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .message("Not Found")
                .build();

        when(httpClientService.executeGetRequest(anyString())).thenReturn(response);

        Pulse pulse = httpUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        assertEquals(target.getId(), pulse.getTarget().getId());
    }

    @Test
    public void shouldReturnUptimeAsDownWhenExceptionOccurs() throws IOException {
        target.setUrl("https://invalidurl.com");
        Exception error = new IOException();
        when(httpClientService.executeGetRequest(anyString())).thenThrow(error);

        Pulse pulse = httpUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        assertEquals(target.getId(), pulse.getTarget().getId());
    }
}
