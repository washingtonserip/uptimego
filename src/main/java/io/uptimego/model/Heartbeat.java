package io.uptimego.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Heartbeat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uptime_config_id")
    private UptimeConfig uptimeConfig;
    private String status;
    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    private HeartbeatDetails details;
    private LocalDateTime timestamp;
}
