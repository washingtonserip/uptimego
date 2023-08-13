package io.uptimego;

import io.hypersistence.tsid.TSID;
import io.uptimego.model.*;

import java.time.LocalDateTime;

public class EntityTestFactory {

    public static User createUser() {
        User user = new User();
        user.setId(TSID.Factory.getTsid().toLong());
        return user;
    }

    public static UptimeConfig createUptimeConfig(User user, String url) {
        UptimeConfig config = new UptimeConfig();
        config.setId(TSID.Factory.getTsid().toLong());
        config.setUser(user);
        config.setUrl(url);
        config.setType(UptimeConfigType.HTTP);
        return config;
    }

    public static UptimeConfig createUptimeConfig(User user, String url, UptimeConfigType type) {
        UptimeConfig config = createUptimeConfig(user, url);
        config.setType(type);
        return config;
    }

    public static Heartbeat createHeartbeat(UptimeConfig uptimeConfig, HeartbeatStatus status, int latency) {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(TSID.Factory.getTsid().toLong());
        heartbeat.setUptimeConfig(uptimeConfig);
        heartbeat.setStatus(status);
        heartbeat.setLatency(latency);
        heartbeat.setTimestamp(LocalDateTime.now());
        return heartbeat;
    }
}
