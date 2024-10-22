package io.uptimego.cron.targetcheck.impl;

import io.uptimego.cron.targetcheck.TargetCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.enums.PulseStatus;
import io.uptimego.model.Target;
import io.uptimego.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class PingTargetCheckStrategy implements TargetCheckStrategy {
    @Autowired
    private NetworkService networkService;

    @Override
    public String getType() {
        return "PING";
    }

    @Override
    public Pulse getPulse(Target target) {
        Pulse pulse = new Pulse();
        pulse.setTarget(target);

        try {
            InetAddress address = networkService.getByName(target.getOptions().getHost());
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
