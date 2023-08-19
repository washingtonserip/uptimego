package io.uptimego.cron.sendalert.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import io.uptimego.cron.sendalert.impl.TelegramNotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
public class TelegramNotificationStrategyTest {

    @InjectMocks
    private TelegramNotificationStrategy telegramNotificationStrategy;

    @Mock
    private RestTemplate restTemplate;
    @Test
    public void testSendNotificationSuccess() throws NoSuchFieldException, IllegalAccessException {
        Notification notification = new Notification();
        Channel channel = new Channel();

        String chatId = "12345";
        String message = "Test message";
        notification.setMessage(message);

        String botToken = "YOUR_BOT_TOKEN";

        // Use reflection to set the botToken field
        Field botTokenField = TelegramNotificationStrategy.class.getDeclaredField("botToken");
        botTokenField.setAccessible(true);
        botTokenField.set(telegramNotificationStrategy, botToken);

        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", botToken, chatId, message);

        when(restTemplate.postForEntity(url, null, String.class)).thenReturn(ResponseEntity.ok().build());

        Notification result = telegramNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.SUCCESS, result.getType());
    }

    @Test
    public void testSendNotificationFailure() {
        Notification notification = new Notification();
        Channel channel = new Channel();

        String chatId = "12345";
        String message = "Test message";
        notification.setMessage(message);

        String url = "https://api.telegram.org/botnull/sendMessage?chat_id=" + chatId + "&text=" + message;

        when(restTemplate.postForEntity(url, null, String.class)).thenThrow(new RuntimeException());

        Notification result = telegramNotificationStrategy.sendNotification(notification, channel);

        assertEquals(NotificationStatus.FAILURE, result.getType());
    }
}
