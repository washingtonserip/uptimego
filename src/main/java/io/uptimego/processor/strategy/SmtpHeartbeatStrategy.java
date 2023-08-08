package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatDetails;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SmtpHeartbeatStrategy implements HeartbeatStrategy {
    static final String EMAIL_FROM = "teste@uptimego.io";

    private EmailService emailService;

    @Override
    public String getType() {
        return "SMTP";
    }

    @Override
    public Heartbeat getHeartbeat(UptimeConfig uptimeConfig) {
        HeartbeatDetails details = new HeartbeatDetails();
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setUptimeConfig(uptimeConfig);

        try {
            UptimeConfigOptions options = uptimeConfig.getOptions();
            String emailTo = options.getEmailTo() != null ? options.getEmailTo() : "test@test.com";
            emailService.sendEmail(options.getHost(), options.getPort(), EMAIL_FROM, emailTo);
            heartbeat.setStatus("up");
        } catch (Exception e) {
            details.setStatusReason(e.getMessage());
            heartbeat.setStatus("down");
        }
        heartbeat.setDetails(details);

        return heartbeat;
    }
}
