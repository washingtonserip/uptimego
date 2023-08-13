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

    public static Pulse createPulse(UptimeConfig uptimeConfig, PulseStatus status, int latency) {
        Pulse pulse = new Pulse();
        pulse.setId(TSID.Factory.getTsid().toLong());
        pulse.setUptimeConfig(uptimeConfig);
        pulse.setStatus(status);
        pulse.setLatency(latency);
        pulse.setTimestamp(LocalDateTime.now());
        return pulse;
    }
}
