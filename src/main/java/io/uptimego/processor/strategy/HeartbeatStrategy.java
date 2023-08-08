package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;

public interface HeartbeatStrategy {
    String getType();

    Heartbeat getHeartbeat(UptimeConfig uptimeConfig);
}
