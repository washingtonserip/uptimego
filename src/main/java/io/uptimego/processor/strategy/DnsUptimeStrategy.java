package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;

import java.net.InetAddress;

public class DnsUptimeStrategy implements UptimeStrategy {

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            InetAddress address = InetAddress.getByName(heartbeat.getUrl());
            String ip = address.getHostAddress();
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}