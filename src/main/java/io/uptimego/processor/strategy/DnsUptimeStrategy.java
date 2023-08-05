package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.NetworkService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

@NoArgsConstructor
@AllArgsConstructor
public class DnsUptimeStrategy implements UptimeStrategy {

    private NetworkService networkService;

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            InetAddress address = networkService.getByName(heartbeat.getUrl());
            String ip = address.getHostAddress();
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}
