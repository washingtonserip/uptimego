package io.uptimego.cron.sendalert.strategy;

import io.uptimego.cron.sendalert.impl.SMSNotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.ChannelMetadata;
import io.uptimego.model.Notification;
import io.uptimego.service.SMSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SMSNotificationStrategyTest {

    @InjectMocks
    private SMSNotificationStrategy smsNotificationStrategy;

    @Mock
    private SMSService smsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotificationWithValidPhoneNumber() {
        Notification notification = new Notification();
        notification.setMessage("Test message");

        Channel channel = new Channel();
        ChannelMetadata metadata = new ChannelMetadata();
        metadata.setPhoneNumber("1234567890");
        channel.setMetadata(metadata);

        when(smsService.sendSMS(anyString(), anyString())).thenReturn(true);

        Notification result = smsNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.SUCCESS, result.getType());
        verify(smsService).sendSMS("1234567890", "Test message");
    }

    @Test
    void testSendNotificationWithEmptyPhoneNumber() {
        Notification notification = new Notification();

        Channel channel = new Channel();
        ChannelMetadata metadata = new ChannelMetadata();
        metadata.setPhoneNumber("");
        channel.setMetadata(metadata);

        Notification result = smsNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.FAILURE, result.getType());
        verify(smsService, never()).sendSMS(anyString(), anyString());
    }

    @Test
    void testSendNotificationWithNullPhoneNumber() {
        Notification notification = new Notification();

        Channel channel = new Channel();

        Notification result = smsNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.FAILURE, result.getType());
        verify(smsService, never()).sendSMS(anyString(), anyString());
    }
}
