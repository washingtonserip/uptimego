package io.uptimego.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "pulses")
@Entity
public class Pulse {

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
    private PulseStatus status;

    private Integer latency;

    @CreationTimestamp
    private LocalDateTime timestamp;

    public boolean isEmpty() {
        return id == null && uptimeConfig == null && status == null;
    }
}
