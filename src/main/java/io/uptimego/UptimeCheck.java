package io.uptimego;

import io.uptimego.domain.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class UptimeCheck {

    private static final Logger logger = LoggerFactory.getLogger(UptimeCheck.class);
    private WebClient webClient;

    private List<String> unavailableKeywords = Arrays.asList("Service is unavailable", "Service not available", "Error occurred");

    public UptimeCheck(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Status checkTarget(int targetId, String targetUrl) {
        logger.info("Checking target: " + targetUrl);

        Mono<String> responseMono = webClient.get().uri(targetUrl).retrieve().bodyToMono(String.class);

        String responseBody = responseMono.block();

        if (responseBody != null && unavailableKeywords.stream().anyMatch(responseBody::contains)) {
            logger.info("Target " + targetUrl + " is not available.");
            return new Status(targetId, false);
        } else {
            logger.info("Target " + targetUrl + " is available.");
            return new Status(targetId, true);
        }
    }
}
