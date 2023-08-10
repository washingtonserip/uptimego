package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class PingHeartbeatStrategy implements HeartbeatStrategy {
    @Autowired
    private NetworkService networkService;

    @Override
    public String getType() {
        return "PING";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            InetAddress address = networkService.getByName(uptimeConfig.getOptions().getHost());
            if (networkService.isReachable(address, 2000)) {
                heartbeat.setStatus(HeartbeatStatus.UP);
            } else {
                heartbeat.setStatus(HeartbeatStatus.DOWN);
            }
        } catch (Exception e) {
            heartbeat.setStatus(HeartbeatStatus.DOWN);
        }

        return heartbeat;
    }
}
