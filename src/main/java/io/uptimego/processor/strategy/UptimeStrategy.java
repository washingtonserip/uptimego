package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;

public interface UptimeStrategy {
    String getType();
    Uptime checkUptime(Heartbeat heartbeat);
}
