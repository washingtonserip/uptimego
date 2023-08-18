package io.uptimego.cron.sendalert.strategy;

import io.uptimego.cron.sendalert.impl.SlackNotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.ChannelMetadata;
import io.uptimego.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class SlackNotificationStrategyTest {

    @InjectMocks
    private SlackNotificationStrategy slackNotificationStrategy;

    @Mock
    private RestTemplate restTemplate;

    private final String webhookUrlBase = "https://hooks.slack.com/services/";

    @BeforeEach
    public void setUp() {
        openMocks(this);
        slackNotificationStrategy = new SlackNotificationStrategy(restTemplate, webhookUrlBase);
    }

    @Test
    public void testSendNotificationSuccess() {
        Notification notification = new Notification();
        notification.setMessage("Test message");
        Channel channel = new Channel();
        ChannelMetadata channelMetadata = new ChannelMetadata();
        channelMetadata.setSlackChannel("testKey"); // Set channel key
        channel.setMetadata(channelMetadata);

        when(restTemplate.postForObject(eq(webhookUrlBase + "testKey"), any(HttpEntity.class), eq(String.class)))
                .thenReturn("ok");

        Notification result = slackNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.SUCCESS, result.getType());
    }

    @Test
    public void testSendNotificationFailure() {
        Notification notification = new Notification();
        Channel channel = new Channel();
        ChannelMetadata channelMetadata = new ChannelMetadata();
        channelMetadata.setSlackChannel("testKey"); // Set channel key
        channel.setMetadata(channelMetadata);

        when(restTemplate.postForObject(eq(webhookUrlBase + "testKey"), any(HttpEntity.class), eq(String.class)))
                .thenReturn("failure");

        Notification result = slackNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.FAILURE, result.getType());
    }

    @Test
    public void testSendNotificationException() {
        Notification notification = new Notification();
        Channel channel = new Channel();
        ChannelMetadata channelMetadata = new ChannelMetadata();
        channelMetadata.setSlackChannel("testKey"); // Set channel key
        channel.setMetadata(channelMetadata);

        when(restTemplate.postForObject(eq(webhookUrlBase + "testKey"), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Exception"));

        Notification result = slackNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.FAILURE, result.getType());
    }
}
