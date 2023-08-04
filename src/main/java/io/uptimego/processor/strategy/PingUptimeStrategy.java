package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;

import java.net.InetAddress;

public class PingUptimeStrategy implements UptimeStrategy {

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            InetAddress address = InetAddress.getByName(heartbeat.getOptions().getHost());
            if (address.isReachable(2000)) {
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
