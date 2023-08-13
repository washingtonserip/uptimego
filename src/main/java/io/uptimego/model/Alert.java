package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "alerts")
@Entity
public class Alert {
    @Id
    private Long alertId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pulse_id")
    private Pulse pulse;

    private String title;
    private String message;
    private String alertType;
    private Integer urgencyLevel;
    private String status;
    private Timestamp createdAt;
    private Timestamp acknowledgedAt;
}
