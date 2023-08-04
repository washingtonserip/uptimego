package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.processor.strategy.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UptimeProcessor {

    private final Map<String, UptimeStrategy> uptimeStrategyMap = new HashMap<>();

    public UptimeProcessor() {
        uptimeStrategyMap.put("HTTP", new HttpUptimeStrategy());
        uptimeStrategyMap.put("HEAD", new HeadUptimeStrategy());
        uptimeStrategyMap.put("TCP", new TcpUptimeStrategy());
        uptimeStrategyMap.put("DNS", new DnsUptimeStrategy());
        uptimeStrategyMap.put("SMTP", new SmtpUptimeStrategy());
        uptimeStrategyMap.put("SSH", new SshUptimeStrategy());
        uptimeStrategyMap.put("PING", new PingUptimeStrategy());
    }

    public Uptime processUptime(Heartbeat heartbeat) {
        UptimeStrategy uptimeStrategy = uptimeStrategyMap.get(heartbeat.getType());
        if (uptimeStrategy == null) {
            throw new IllegalArgumentException("Unsupported heartbeat type: " + heartbeat.getType());
        }
        return uptimeStrategy.checkUptime(heartbeat);
    }
}
