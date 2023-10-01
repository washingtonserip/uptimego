package io.uptimego.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import io.uptimego.enums.AlertStatus;
import io.uptimego.enums.AlertType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Table(name = "alerts")
@Entity
public class Alert {

    @Id
    @Tsid
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pulse_id")
    private Pulse pulse;

    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime acknowledgedAt;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL)
    private Set<Notification> notifications;

}
