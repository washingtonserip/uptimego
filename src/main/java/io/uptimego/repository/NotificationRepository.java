package io.uptimego.repository;

import io.uptimego.model.Alert;
import io.uptimego.model.Notification;
import io.uptimego.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser(User user);
}
