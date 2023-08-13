package io.uptimego.cron.targetcheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
import io.uptimego.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.SendFailedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmtpTargetCheckJobStrategyTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SmtpTargetCheckStrategy smtpUptimeStrategy;

    private Target target;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        target = EntityTestFactory.createTarget(user, "https://uptimego.io", TargetType.SMTP);
        target.setOptions(new TargetOptions());
        target.getOptions().setPort(25);
        target.getOptions().setEmailTo("test@test.com");
    }

    @Test
    void shouldReturnUptimeAsUpWhenEmailIsSent() throws Exception {
        target.getOptions().setHost("smtp.example.com");
        Pulse pulse = smtpUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.UP, pulse.getStatus());
        verify(emailService, times(1)).sendEmail("smtp.example.com", 25, SmtpTargetCheckStrategy.EMAIL_FROM, "test@test.com");
    }

    @Test
    void shouldReturnUptimeAsDownWhenEmailCannotBeSent() throws Exception {
        Exception error = new SendFailedException("No recipient addresses");
        doThrow(error).when(emailService).sendEmail(null, 25, SmtpTargetCheckStrategy.EMAIL_FROM, "test@test.com");

        Pulse pulse = smtpUptimeStrategy.getPulse(target);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        verify(emailService, times(1)).sendEmail(null, 25, SmtpTargetCheckStrategy.EMAIL_FROM, "test@test.com");
    }
}
