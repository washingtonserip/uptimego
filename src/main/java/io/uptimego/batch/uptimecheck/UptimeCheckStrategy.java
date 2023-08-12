package io.uptimego.batch.uptimecheck;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;

public interface UptimeCheckStrategy {
    String getType();

    Heartbeat getHeartbeat(UptimeConfig uptimeConfig);
}
