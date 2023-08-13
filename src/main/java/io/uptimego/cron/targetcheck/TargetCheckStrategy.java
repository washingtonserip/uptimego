package io.uptimego.cron.targetcheck;

import io.uptimego.model.Pulse;
import io.uptimego.model.UptimeConfig;

public interface TargetCheckStrategy {
    String getType();

    Pulse getPulse(UptimeConfig uptimeConfig);
}
