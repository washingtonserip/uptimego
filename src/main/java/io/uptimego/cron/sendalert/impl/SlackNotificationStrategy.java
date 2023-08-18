package io.uptimego.cron.sendalert.impl;

import io.uptimego.cron.sendalert.NotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackNotificationStrategy implements NotificationStrategy {
    private static final Logger log = LoggerFactory.getLogger(SlackNotificationStrategy.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final String webhookUrl = "https://hooks.slack.com/services/";

    @Override
    public String getType() {
        return "SLACK";
    }

    @Override
    public Notification sendNotification(Notification notification, Channel channel) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String payload = "{\"text\":\"" + notification.getMessage() + "\"}";
            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            var result = restTemplate.postForObject(webhookUrl, request, String.class);

            if (result != null && result.equals("ok")) {
                notification.setType(NotificationStatus.SUCCESS);
            } else {
                notification.setType(NotificationStatus.FAILURE);
            }
        } catch (Exception e) {
            notification.setType(NotificationStatus.FAILURE);
            log.error("Error while sending notification to Slack", e);
        }

        return notification;
    }
}
