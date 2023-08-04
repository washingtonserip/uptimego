package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;

import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpUptimeStrategy implements UptimeStrategy {

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try (Socket socket = new Socket()) {
            HeartbeatOptions options = heartbeat.getOptions();
            socket.connect(new InetSocketAddress(options.getHost(), options.getPort()), 2000);
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}