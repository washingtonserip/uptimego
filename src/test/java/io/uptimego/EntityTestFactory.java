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

    public static Target createTarget(User user, String url) {
        Target config = new Target();
        config.setId(TSID.Factory.getTsid().toLong());
        config.setUser(user);
        config.setUrl(url);
        config.setType(TargetType.HTTP);
        return config;
    }

    public static Target createTarget(User user, String url, TargetType type) {
        Target config = createTarget(user, url);
        config.setType(type);
        return config;
    }

    public static Pulse createPulse(Target target, PulseStatus status, int latency) {
        Pulse pulse = new Pulse();
        pulse.setId(TSID.Factory.getTsid().toLong());
        pulse.setTarget(target);
        pulse.setStatus(status);
        pulse.setLatency(latency);
        pulse.setTimestamp(LocalDateTime.now());
        return pulse;
    }

    public static Alert buildAlert(Pulse pulse) {
        Alert alert = new Alert();
        alert.setUser(pulse.getTarget().getUser());
        alert.setPulse(pulse);
        alert.setAlertType(AlertType.DOWNTIME);
        alert.setStatus(AlertStatus.CREATED);
        return alert;
    }
}
