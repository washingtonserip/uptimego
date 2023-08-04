package io.uptimego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Uptime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long heartbeatId;

    private String status;

    private int responseCode;

    private double responseTime;

    private LocalDateTime timestamp;
}
