package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.service.SshService;
import org.springframework.beans.factory.annotation.Autowired;

public class SshHeartbeatStrategy implements HeartbeatStrategy {

    @Autowired
    private SshService sshService;

    @Override
    public String getType() {
        return "SSH";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            UptimeConfigOptions options = uptimeConfig.getOptions();
            sshService.establishSshSession(options.getUsername(), options.getHost(), options.getPassword());
            heartbeat.setStatus("up");
        } catch (Exception e) {
            details.setStatusReason(e.getMessage());
            heartbeat.setStatus("down");
        }
        heartbeat.setDetails(details);

        return heartbeat;
    }
}
