package io.uptimego.batch.uptimecheck.impl;

import io.uptimego.batch.uptimecheck.UptimeCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpUptimeCheckStrategy implements UptimeCheckStrategy {

    @Autowired
    private SocketService socketService;

    @Override
    public String getType() {
        return "TCP";
    }

    @Override
    public Pulse getPulse(UptimeConfig uptimeConfig) {
        Pulse pulse = new Pulse();
        pulse.setUptimeConfig(uptimeConfig);

        try {
            UptimeConfigOptions options = uptimeConfig.getOptions();
            socketService.connectSocket(options.getHost(), options.getPort());
            pulse.setStatus(PulseStatus.UP);
        } catch (Exception e) {
            pulse.setStatus(PulseStatus.DOWN);
        }

        return pulse;
    }
}