package io.uptimego.model;

import io.uptimego.utils.JsonbConverter;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
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
