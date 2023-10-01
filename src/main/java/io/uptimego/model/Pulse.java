package io.uptimego.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import io.uptimego.enums.PulseStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "pulses")
@Entity
public class Pulse {

    @Id
    @Tsid
    private Long id;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Target target;

    @Enumerated(EnumType.STRING)
    private PulseStatus status;

    private Integer latency;

    @CreationTimestamp
    private LocalDateTime timestamp;

    public boolean isEmpty() {
        return id == null && target == null && status == null;
    }
}
