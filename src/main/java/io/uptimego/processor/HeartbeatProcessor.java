package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.processor.strategy.HeartbeatStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HeartbeatProcessor {

    private final Map<String, HeartbeatStrategy> heartbeatStrategyMap;

    @Autowired
    public HeartbeatProcessor(List<HeartbeatStrategy> heartbeatStrategies) {
        this.heartbeatStrategyMap = heartbeatStrategies.stream()
                .collect(Collectors.toMap(HeartbeatStrategy::getType, Function.identity()));
    }

    public Heartbeat execute(UptimeConfig uptimeConfig) {
        HeartbeatStrategy heartbeatStrategy = heartbeatStrategyMap.get(uptimeConfig.getType().name());
        if (heartbeatStrategy == null) {
            throw new IllegalArgumentException("Unsupported uptime config type: " + uptimeConfig.getType());
        }
        return heartbeatStrategy.getHeartbeat(uptimeConfig);
    }
}
