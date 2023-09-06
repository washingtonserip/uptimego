package io.uptimego.controller;

import io.uptimego.model.Notification;
import io.uptimego.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public List<Notification> getAll() {
        return service.findByCurrentUser();
    }

}
