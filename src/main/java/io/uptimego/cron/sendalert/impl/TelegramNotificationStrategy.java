package io.uptimego.cron.sendalert.impl;

import io.uptimego.cron.sendalert.NotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TelegramNotificationStrategy implements NotificationStrategy {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getType() {
        return "TELEGRAM";
    }

    public Notification sendNotification(Notification notification, Channel channel) {
        try {
            String chatId = getChatId(channel);
            String message = notification.getMessage();

            String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", botToken, chatId, message);

            restTemplate.postForEntity(url, null, String.class);

            notification.setType(NotificationStatus.SUCCESS);
        } catch (Exception e) {
            notification.setType(NotificationStatus.FAILURE);
            // Log error
        }
        return notification;
    }

    private String getChatId(Channel channel) {
        // Logic to retrieve the chat ID from the channel
        // ...

        return "12345";
    }
}
