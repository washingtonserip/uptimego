package io.uptimego.model;

import io.uptimego.util.JsonbConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UptimeConfig getUptimeConfig() {
        return uptimeConfig;
    }

    public void setUptimeConfig(UptimeConfig uptimeConfig) {
        this.uptimeConfig = uptimeConfig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HeartbeatDetails getDetails() {
        return details;
    }

    public void setDetails(HeartbeatDetails details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Heartbeat heartbeat)) return false;
        return Objects.equals(getId(), heartbeat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
