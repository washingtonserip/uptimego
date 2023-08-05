package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;
import io.uptimego.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SmtpUptimeStrategyTest {

    private SmtpUptimeStrategy smtpUptimeStrategy;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        smtpUptimeStrategy = new SmtpUptimeStrategy(emailService);
    }

    @Test
    void shouldReturnUptimeAsUpWhenEmailIsSent() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("smtp.example.com");
        heartbeat.getOptions().setPort(25);
        heartbeat.getOptions().setEmailTo("test@test.com");

        Uptime uptime = smtpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
        verify(emailService, times(1)).sendEmail("smtp.example.com", 25, SmtpUptimeStrategy.EMAIL_FROM, "test@test.com");
    }

    @Test
    void shouldReturnUptimeAsDownWhenEmailCannotBeSent() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("smtp.example.com");
        heartbeat.getOptions().setPort(25);
        heartbeat.getOptions().setEmailTo("test@test.com");

        doThrow(new Exception()).when(emailService).sendEmail("smtp.example.com", 25, SmtpUptimeStrategy.EMAIL_FROM, "test@test.com");

        Uptime uptime = smtpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        verify(emailService, times(1)).sendEmail("smtp.example.com", 25, SmtpUptimeStrategy.EMAIL_FROM, "test@test.com");
    }
}
