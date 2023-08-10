package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private double latency;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
