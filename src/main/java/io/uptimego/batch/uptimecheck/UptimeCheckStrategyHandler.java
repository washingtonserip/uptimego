package io.uptimego.batch.uptimecheck;

import io.uptimego.model.Pulse;
import io.uptimego.model.UptimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UptimeCheckStrategyHandler {

    private final Map<String, UptimeCheckStrategy> uptimeCheckStrategyMap;

    @Autowired
    public UptimeCheckStrategyHandler(List<UptimeCheckStrategy> uptimeCheckStrategies) {
        this.uptimeCheckStrategyMap = uptimeCheckStrategies.stream()
                .collect(Collectors.toMap(UptimeCheckStrategy::getType, Function.identity()));
    }

    public Pulse execute(UptimeConfig uptimeConfig) {
        UptimeCheckStrategy uptimeCheckStrategy = uptimeCheckStrategyMap.get(uptimeConfig.getType().name());
        if (uptimeCheckStrategy == null) {
            throw new IllegalArgumentException("Unsupported uptime config type: " + uptimeConfig.getType());
        }
        return uptimeCheckStrategy.getPulse(uptimeConfig);
    }
}
