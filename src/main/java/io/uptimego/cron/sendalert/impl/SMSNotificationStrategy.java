package io.uptimego.cron.sendalert.impl;

import io.uptimego.cron.sendalert.NotificationStrategy;
import io.uptimego.enums.NotificationStatus;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import io.uptimego.service.SMSService;
import io.uptimego.service.impl.SMSServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static io.uptimego.enums.NotificationStatus.FAILURE;

@Component
@RequiredArgsConstructor
public class SMSNotificationStrategy implements NotificationStrategy {
    private static final Logger log = LoggerFactory.getLogger(SMSServiceImpl.class);

    private final SMSService smsService;

    @Override
    public String getType() {
        return "SMS";
    }

    @Override
    public Notification sendNotification(Notification notification, Channel channel) {
        String phoneNumber = channel.getMetadata() != null ? channel.getMetadata().getPhoneNumber() : null;
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            log.warn("Channel has an empty phone number. Not sending phone number.");
            notification.setType(FAILURE);
            return notification;
        }

        String message = notification.getMessage();
        boolean success = smsService.sendSMS(phoneNumber, message);
        notification.setType(success ? NotificationStatus.SUCCESS : NotificationStatus.FAILURE);

        return notification;
    }
}
