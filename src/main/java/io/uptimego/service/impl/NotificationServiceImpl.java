package io.uptimego.service.impl;

import io.uptimego.model.Notification;
import io.uptimego.model.User;
import io.uptimego.repository.NotificationRepository;
import io.uptimego.security.service.UserService;
import io.uptimego.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final UserService userService;

    @Override
    public List<Notification> findByCurrentUser() {
        User user = userService.getCurrentUser();
        return repository.findByUser(user);
    }
}
