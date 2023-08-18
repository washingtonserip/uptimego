package io.uptimego.cron.sendalert.strategy;

import io.uptimego.cron.sendalert.impl.SlackNotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SlackNotificationStrategyTest {

    @InjectMocks
    private SlackNotificationStrategy slackNotificationStrategy;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Scenario 1: Test Successful Notification Send
    @Test
    void testSendNotification_Successful() {
        Notification notification = new Notification();
        notification.setMessage("Test message");
        Channel channel = new Channel();

        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn("OK");

        Notification result = slackNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.SUCCESS, result.getType());
    }
    @Test
    void testSendNotification_Failure() {
        Notification notification = new Notification();
        notification.setMessage("Test message");
        Channel channel = new Channel();

        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn("Gateway Timeout");

        Notification result = slackNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.FAILURE, result.getType());
    }

}
