package io.uptimego.processor.strategy;

import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.SshService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SshUptimeStrategy implements UptimeStrategy {

    private SshService sshService;

    @Override
    public String getType() {
        return "SSH";
    }

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            HeartbeatOptions options = heartbeat.getOptions();
            sshService.establishSshSession(options.getUsername(), options.getHost(), options.getPassword());
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}
