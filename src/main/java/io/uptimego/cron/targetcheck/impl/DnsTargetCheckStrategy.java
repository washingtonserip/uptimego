package io.uptimego.cron.targetcheck.impl;

import io.uptimego.cron.targetcheck.TargetCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.model.Target;
import io.uptimego.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class DnsTargetCheckStrategy implements TargetCheckStrategy {

    @Autowired
    private NetworkService networkService;

    @Override
    public String getType() {
        return "DNS";
    }

    @Override
    public Pulse getPulse(Target target) {
        Pulse pulse = new Pulse();
        pulse.setTarget(target);

        try {
            InetAddress address = networkService.getByName(target.getUrl());
            pulse.setStatus(PulseStatus.UP);
        } catch (Exception e) {
            pulse.setStatus(PulseStatus.DOWN);
        }

        return pulse;
    }
}
