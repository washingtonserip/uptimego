package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatType;
import io.uptimego.model.Uptime;
import io.uptimego.service.NetworkService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

@AllArgsConstructor
@NoArgsConstructor
public class PingUptimeStrategy implements UptimeStrategy {
    private NetworkService networkService;

    @Override
    public String getType() {
        return "PING";
    }

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            InetAddress address = networkService.getByName(heartbeat.getOptions().getHost());
            if (networkService.isReachable(address, 2000)) {
                uptime.setStatus("up");
            } else {
                uptime.setStatus("down");
            }
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}
