package io.uptimego.processor.strategy;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;


public class SshUptimeStrategy implements UptimeStrategy {

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            HeartbeatOptions options = heartbeat.getOptions();
            JSch jsch = new JSch();
            Session session = jsch.getSession(options.getUsername(), options.getHost(), 22);
            session.setPassword(options.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.disconnect();
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}