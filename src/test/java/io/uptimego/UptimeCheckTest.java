package io.uptimego;

import io.uptimego.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UptimeCheckTest {

    private UptimeCheck uptimeCheck;
    private WebClient webClient;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setup() {
        webClient = Mockito.mock(WebClient.class);
        requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        uptimeCheck = new UptimeCheck(WebClient.builder());
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void shouldReturnStatusAsTrueWhenTargetIsAvailable() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("OK"));

        Status status = uptimeCheck.checkTarget(1, "http://targetAvailable.com");

        assertTrue(status.isAvailable());
    }

    @Test
    public void shouldReturnStatusAsFalseWhenTargetIsUnavailable() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Service is unavailable"));

        Status status = uptimeCheck.checkTarget(2, "http://targetUnavailable.com");

        assertFalse(status.isAvailable());
    }

    @Test
    public void shouldReturnStatusAsFalseWhenHttpClientThrowsError() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException()));

        Status status = uptimeCheck.checkTarget(3, "http://targetError.com");

        assertFalse(status.isAvailable());
    }

    @Test
    public void shouldReturnStatusAsFalseWhenHttpClientReturnsNon200StatusCode() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Not Found"));

        Status status = uptimeCheck.checkTarget(4, "http://targetNotFound.com");

        assertFalse(status.isAvailable());
    }

}
