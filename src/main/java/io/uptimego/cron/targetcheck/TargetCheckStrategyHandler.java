package io.uptimego.cron.targetcheck;

import io.uptimego.model.Pulse;
import io.uptimego.model.UptimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TargetCheckStrategyHandler {

    private final Map<String, TargetCheckStrategy> targetCheckStrategyMap;

    @Autowired
    public TargetCheckStrategyHandler(List<TargetCheckStrategy> targetCheckStrategies) {
        this.targetCheckStrategyMap = targetCheckStrategies.stream()
                .collect(Collectors.toMap(TargetCheckStrategy::getType, Function.identity()));
    }

    public Pulse execute(UptimeConfig uptimeConfig) {
        TargetCheckStrategy targetCheckStrategy = targetCheckStrategyMap.get(uptimeConfig.getType().name());
        if (targetCheckStrategy == null) {
            throw new IllegalArgumentException("Unsupported uptime config type: " + uptimeConfig.getType());
        }
        return targetCheckStrategy.getPulse(uptimeConfig);
    }
}
