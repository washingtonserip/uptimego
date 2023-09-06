package io.uptimego.repository;

import io.uptimego.model.Notification;
import io.uptimego.model.User;

import java.util.List;

public interface NotificationRepository {

    List<Notification> findByUser(User user);
}
