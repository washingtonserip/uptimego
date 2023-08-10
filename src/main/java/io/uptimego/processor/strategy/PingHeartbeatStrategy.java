package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
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
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            InetAddress address = networkService.getByName(uptimeConfig.getOptions().getHost());
            details.setIpAddress(address.getHostAddress());
            if (networkService.isReachable(address, 2000)) {
                heartbeat.setStatus("up");
            } else {
                details.setStatusReason("Host is not reachable");
                heartbeat.setStatus("down");
            }
        } catch (Exception e) {
            details.setStatusReason(e.getMessage());
            heartbeat.setStatus("down");
        }
        heartbeat.setDetails(details);

        return heartbeat;
    }
}
