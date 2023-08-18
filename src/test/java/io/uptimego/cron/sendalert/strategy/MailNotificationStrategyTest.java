package io.uptimego.cron.sendalert.strategy;

import io.uptimego.cron.sendalert.impl.MailNotificationStrategy;
import io.uptimego.model.ChannelMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import io.uptimego.model.Channel;
import io.uptimego.model.Notification;

import static io.uptimego.enums.NotificationStatus.FAILURE;
import static io.uptimego.enums.NotificationStatus.SUCCESS;

@ExtendWith(MockitoExtension.class)
public class MailNotificationStrategyTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private MailNotificationStrategy mailNotificationStrategy;

    private Notification notification;
    private Channel channel;

    @BeforeEach
    public void setUp() {
        notification = new Notification();
        notification.setTitle("This is an alert title");
        notification.setMessage("This is an alert message");
        channel = new Channel();
        channel.setMetadata(new ChannelMetadata());
        channel.getMetadata().setEmailTo("test@example.com");
    }

    @Test
    public void testSendNotification_Success() {
        Notification result = mailNotificationStrategy.sendNotification(notification, channel);

        assertEquals(SUCCESS, result.getType());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendNotification_Failure() {
        doThrow(new MailSendException("Test exception")).when(emailSender).send(any(SimpleMailMessage.class));

        Notification result = mailNotificationStrategy.sendNotification(notification, channel);

        assertEquals(FAILURE, result.getType());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }


    @Test
    public void testSendNotification_NullEmail() {
        channel.getMetadata().setEmailTo(null);

        Notification result = mailNotificationStrategy.sendNotification(notification, channel);

        assertEquals(FAILURE, result.getType());
        verify(emailSender, times(0)).send(any(SimpleMailMessage.class));
    }
}
