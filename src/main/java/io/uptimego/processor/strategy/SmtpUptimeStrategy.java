package io.uptimego.processor.strategy;

import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SmtpUptimeStrategy implements UptimeStrategy {
    static final String EMAIL_FROM = "teste@uptimego.io";

    private EmailService emailService;

    @Override
    public String getType() {
        return "SMTP";
    }

    @Override
    public Uptime checkUptime(Heartbeat heartbeat) {
        Uptime uptime = new Uptime();
        uptime.setHeartbeatId(heartbeat.getId());

        try {
            HeartbeatOptions options = heartbeat.getOptions();
            String emailTo = options.getEmailTo() != null ? options.getEmailTo() : "test@test.com";
            emailService.sendEmail(options.getHost(), options.getPort(), EMAIL_FROM, emailTo);
            uptime.setStatus("up");
        } catch (Exception e) {
            uptime.setStatus("down");
        }

        return uptime;
    }
}
