package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.NetworkService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

@NoArgsConstructor
@AllArgsConstructor
public class DnsHeartbeatStrategy implements HeartbeatStrategy {

    private NetworkService networkService;

    @Override
    public String getType() {
        return "DNS";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            InetAddress address = networkService.getByName(uptimeConfig.getUrl());
            details.setIpAddress(address.getHostAddress());
            heartbeat.setStatus("up");
        } catch (Exception e) {
            details.setStatusReason(e.getMessage());
            heartbeat.setStatus("down");
        }
        heartbeat.setDetails(details);

        return heartbeat;
    }
}
