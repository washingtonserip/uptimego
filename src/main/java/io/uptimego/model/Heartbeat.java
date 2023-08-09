package io.uptimego.model;

import io.uptimego.utils.JsonbConverter;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Convert(converter = JsonbConverter.class)
    private HeartbeatDetails details;
    private LocalDateTime timestamp;
}
