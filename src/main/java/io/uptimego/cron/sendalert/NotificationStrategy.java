package io.uptimego.cron.sendalert;

import io.uptimego.model.Channel;
import io.uptimego.model.Notification;

public interface NotificationStrategy {
    String getType();

    Notification sendNotification(Notification notification, Channel channel);
}
