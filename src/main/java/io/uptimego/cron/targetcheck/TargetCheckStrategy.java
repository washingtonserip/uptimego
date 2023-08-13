package io.uptimego.cron.targetcheck;

import io.uptimego.model.Pulse;
import io.uptimego.model.Target;

public interface TargetCheckStrategy {
    String getType();

    Pulse getPulse(Target target);
}
