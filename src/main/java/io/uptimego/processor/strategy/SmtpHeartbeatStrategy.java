package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmtpHeartbeatStrategy implements HeartbeatStrategy {
    static final String EMAIL_FROM = "teste@uptimego.io";

    @Autowired
    private EmailService emailService;

    @Override
    public String getType() {
        return "SMTP";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            UptimeConfigOptions options = uptimeConfig.getOptions();
            String emailTo = options.getEmailTo() != null ? options.getEmailTo() : "test@test.com";
            emailService.sendEmail(options.getHost(), options.getPort(), EMAIL_FROM, emailTo);
            heartbeat.setStatus(HeartbeatStatus.UP);
        } catch (Exception e) {
            heartbeat.setStatus(HeartbeatStatus.DOWN);
        }

        return heartbeat;
    }
}
