package io.uptimego.batch.uptimecheck.impl;

import io.uptimego.batch.uptimecheck.UptimeCheckStrategy;
import io.uptimego.model.Pulse;
import io.uptimego.model.PulseStatus;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmtpUptimeCheckStrategy implements UptimeCheckStrategy {
    static final String EMAIL_FROM = "teste@uptimego.io";

    @Autowired
    private EmailService emailService;

    @Override
    public String getType() {
        return "SMTP";
    }

    @Override
    public Pulse getPulse(UptimeConfig uptimeConfig) {
        Pulse pulse = new Pulse();
        pulse.setUptimeConfig(uptimeConfig);

        try {
            UptimeConfigOptions options = uptimeConfig.getOptions();
            String emailTo = options.getEmailTo() != null ? options.getEmailTo() : "test@test.com";
            emailService.sendEmail(options.getHost(), options.getPort(), EMAIL_FROM, emailTo);
            pulse.setStatus(PulseStatus.UP);
        } catch (Exception e) {
            pulse.setStatus(PulseStatus.DOWN);
        }

        return pulse;
    }
}
