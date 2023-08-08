package io.uptimego.model;

import io.uptimego.util.JsonbConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Heartbeat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uptime_config_id")
    private UptimeConfig uptimeConfig;

    private String status;

    private double responseTime;

    @Convert(converter = JsonbConverter.class)
    private HeartbeatDetails details;

    private LocalDateTime timestamp;
}
