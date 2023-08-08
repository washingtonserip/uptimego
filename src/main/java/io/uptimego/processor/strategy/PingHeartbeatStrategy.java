package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.NetworkService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

@AllArgsConstructor
@NoArgsConstructor
public class PingHeartbeatStrategy implements HeartbeatStrategy {
    private NetworkService networkService;

    @Override
    public String getType() {
        return "PING";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeId(uptimeConfig.getId());

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
