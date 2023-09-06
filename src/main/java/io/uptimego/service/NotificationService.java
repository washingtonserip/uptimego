package io.uptimego.service;

import io.uptimego.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> findByCurrentUser();

}
