package io.uptimego.model;

import io.uptimego.util.JsonbConverter;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UptimeConfigType getType() {
        return type;
    }

    public void setType(UptimeConfigType type) {
        this.type = type;
    }

    public UptimeConfigOptions getOptions() {
        return options;
    }

    public void setOptions(UptimeConfigOptions options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UptimeConfig that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "UptimeConfig{" +
                "id=" + id +
                ", user=" + user +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", options=" + options +
                '}';
    }
}
