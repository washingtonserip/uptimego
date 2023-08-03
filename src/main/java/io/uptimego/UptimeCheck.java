package io.uptimego;

import io.uptimego.domain.Target;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UptimeCheck {

    private final WebClient webClient;

    public UptimeCheck(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CheckResult> checkTarget(Target target) {
        return webClient.get()
                .uri(target.getDomain())
                .exchangeToMono(response -> {
                    HttpStatus status = response.statusCode();
                    if (status.is2xxSuccessful()) {
                        return response.bodyToMono(String.class)
                                .map(body -> new CheckResult(target.getId(), status.value(), body));
                    } else {
                        return Mono.just(new CheckResult(target.getId(), status.value(), "Service is down"));
                    }
                });
    }

    public static class CheckResult {
        private final Long targetId;
        private final int status;
        private final String body;

        public CheckResult(Long targetId, int status, String body) {
            this.targetId = targetId;
            this.status = status;
            this.body = body;
        }

        public Long getTargetId() {
            return targetId;
        }

        public int getStatus() {
            return status;
        }

        public String getBody() {
            return body;
        }
    }
}
