package io.uptimego.model;

import io.uptimego.util.JsonbConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptimeConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    @Enumerated(EnumType.STRING)
    private UptimeConfigType type;

    @Convert(converter = JsonbConverter.class)
    private UptimeConfigOptions options;
}
