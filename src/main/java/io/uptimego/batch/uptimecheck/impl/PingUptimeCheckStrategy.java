package io.uptimego.batch.uptimecheck.impl;

import io.uptimego.batch.uptimecheck.UptimeCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class PingUptimeCheckStrategy implements UptimeCheckStrategy {
    @Autowired
    private NetworkService networkService;

    @Override
    public String getType() {
        return "PING";
    }

    @Override
    public Pulse getPulse(UptimeConfig uptimeConfig) {
        Pulse pulse = new Pulse();
        pulse.setUptimeConfig(uptimeConfig);

        try {
            InetAddress address = networkService.getByName(uptimeConfig.getOptions().getHost());
            if (networkService.isReachable(address, 2000)) {
                pulse.setStatus(PulseStatus.UP);
            } else {
                pulse.setStatus(PulseStatus.DOWN);
            }
        } catch (Exception e) {
            pulse.setStatus(PulseStatus.DOWN);
        }

        return pulse;
    }
}
