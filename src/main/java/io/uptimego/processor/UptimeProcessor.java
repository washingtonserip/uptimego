package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.processor.strategy.UptimeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UptimeProcessor {

    private final Map<String, UptimeStrategy> uptimeStrategyMap;

    @Autowired
    public UptimeProcessor(List<UptimeStrategy> uptimeStrategies) {
        this.uptimeStrategyMap = uptimeStrategies.stream()
                .collect(Collectors.toMap(UptimeStrategy::getType, Function.identity()));
    }

    public Uptime processUptime(Heartbeat heartbeat) {
        UptimeStrategy uptimeStrategy = uptimeStrategyMap.get(heartbeat.getType().name());
        if (uptimeStrategy == null) {
            throw new IllegalArgumentException("Unsupported heartbeat type: " + heartbeat.getType());
        }
        return uptimeStrategy.checkUptime(heartbeat);
    }
}
