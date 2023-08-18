package io.uptimego.cron.sendalert.impl;

import io.uptimego.cron.sendalert.NotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Component
public class SlackNotificationStrategy implements NotificationStrategy {
    private static final Logger log = LoggerFactory.getLogger(SlackNotificationStrategy.class);
    private static final String SLACK_TYPE = "SLACK";

    private final RestTemplate restTemplate;
    private final String webhookUrl;

    public SlackNotificationStrategy(RestTemplate restTemplate, @Value("${slack.webhook.url}") String webhookUrl) {
        this.restTemplate = restTemplate;
        this.webhookUrl = webhookUrl;
    }

    @Override
    public String getType() {
        return SLACK_TYPE;
    }

    @Override
    public Notification sendNotification(Notification notification, Channel channel) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> payload = Map.of("text", notification.getMessage());
            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

            String channelKey = channel.getMetadata().getSlackChannel(); // Retrieve channel key
            String fullWebhookUrl = webhookUrl + channelKey; // Append channel key to webhook URL

            var result = Optional.ofNullable(restTemplate.postForObject(fullWebhookUrl, request, String.class));

            notification.setType(result.isPresent() && result.get().equals("ok")
                    ? NotificationStatus.SUCCESS
                    : NotificationStatus.FAILURE);
        } catch (Exception e) {
            notification.setType(NotificationStatus.FAILURE);
            log.error("Error while sending notification to Slack: {}", e.getMessage(), e);
        }

        return notification;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
