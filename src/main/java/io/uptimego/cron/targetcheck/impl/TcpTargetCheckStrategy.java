package io.uptimego.cron.targetcheck.impl;

import io.uptimego.cron.targetcheck.TargetCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.enums.PulseStatus;
import io.uptimego.model.Target;
import io.uptimego.model.TargetOptions;
import io.uptimego.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpTargetCheckStrategy implements TargetCheckStrategy {

    @Autowired
    private SocketService socketService;

    @Override
    public String getType() {
        return "TCP";
    }

    @Override
    public Pulse getPulse(Target target) {
        Pulse pulse = new Pulse();
        pulse.setTarget(target);

        try {
            TargetOptions options = target.getOptions();
            socketService.connectSocket(options.getHost(), options.getPort());
            pulse.setStatus(PulseStatus.UP);
        } catch (Exception e) {
            pulse.setStatus(PulseStatus.DOWN);
        }

        return pulse;
    }
}
