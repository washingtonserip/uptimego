package io.uptimego.model;

import io.uptimego.util.JsonbConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "uptime_config")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptimeConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    private UUID userId;

    private String url;

    @Enumerated(EnumType.STRING)
    private UptimeConfigType type;

    @Convert(converter = JsonbConverter.class)
    private UptimeConfigOptions options;
}
