package io.uptimego.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Heartbeat {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(
            name = "tsid",
            strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uptime_config_id")
    private UptimeConfig uptimeConfig;

    @Enumerated(EnumType.STRING)
    private HeartbeatStatus status;

    private double responseTime;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
