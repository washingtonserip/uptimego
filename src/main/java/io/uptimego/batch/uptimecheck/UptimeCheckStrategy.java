package io.uptimego.batch.uptimecheck;

import io.uptimego.model.Pulse;
import io.uptimego.model.UptimeConfig;

public interface UptimeCheckStrategy {
    String getType();

    Pulse getPulse(UptimeConfig uptimeConfig);
}
