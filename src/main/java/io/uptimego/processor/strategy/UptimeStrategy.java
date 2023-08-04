package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;

public interface UptimeStrategy {
    Uptime checkUptime(Heartbeat heartbeat);
}
