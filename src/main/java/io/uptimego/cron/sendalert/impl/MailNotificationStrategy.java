package io.uptimego.cron.sendalert.impl;

import io.uptimego.cron.sendalert.NotificationStrategy;
import io.uptimego.model.Channel;
import io.uptimego.model.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static io.uptimego.enums.NotificationStatus.FAILURE;
import static io.uptimego.enums.NotificationStatus.SUCCESS;

@Component
@RequiredArgsConstructor
public class MailNotificationStrategy implements NotificationStrategy {
    private static final Logger log = LoggerFactory.getLogger(MailNotificationStrategy.class);

    private final JavaMailSender emailSender;

    @Override
    public String getType() {
        return "MAIL";
    }

    @Override
    public Notification sendNotification(Notification notification, Channel channel) {
        String emailTo = channel.getMetadata() != null ? channel.getMetadata().getEmailTo() : null;
        if (emailTo == null || emailTo.trim().isEmpty()) {
            log.warn("Channel has an empty email. Not sending email.");
            notification.setType(FAILURE);
            return notification;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailTo);
            mailMessage.setSubject(notification.getTitle());
            mailMessage.setText(notification.getMessage());
            emailSender.send(mailMessage);
            notification.setType(SUCCESS);
            return notification;
        } catch (MailException e) {
            log.error("Failed to send email alert");
            notification.setType(FAILURE);
            return notification;
        }
    }
}
