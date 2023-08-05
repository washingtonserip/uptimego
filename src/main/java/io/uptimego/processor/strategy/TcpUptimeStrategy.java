package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.HeartbeatType;
import io.uptimego.model.Uptime;
import io.uptimego.service.SocketService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TcpUptimeStrategy implements UptimeStrategy {

    private SocketService socketService;

    @Override
    public String getType() {
        return "TCP";
    }

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            HeartbeatOptions options = heartbeat.getOptions();
            socketService.connectSocket(options.getHost(), options.getPort());
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}
