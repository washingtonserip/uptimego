package io.uptimego.batch.uptimecheck.impl;

import io.hypersistence.tsid.TSID;
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
class SmtpUptimeCheckStrategyTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SmtpUptimeCheckStrategy smtpUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(TSID.Factory.getTsid().toLong());
        uptimeConfig.setUser(user);
        uptimeConfig.setType(UptimeConfigType.SMTP);
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setPort(25);
        uptimeConfig.getOptions().setEmailTo("test@test.com");
    }

    @Test
    void shouldReturnUptimeAsUpWhenEmailIsSent() throws Exception {
        uptimeConfig.getOptions().setHost("smtp.example.com");
        Heartbeat heartbeat = smtpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.UP, heartbeat.getStatus());
        verify(emailService, times(1)).sendEmail("smtp.example.com", 25, SmtpUptimeCheckStrategy.EMAIL_FROM, "test@test.com");
    }

    @Test
    void shouldReturnUptimeAsDownWhenEmailCannotBeSent() throws Exception {
        Exception error = new SendFailedException("No recipient addresses");
        doThrow(error).when(emailService).sendEmail(null, 25, SmtpUptimeCheckStrategy.EMAIL_FROM, "test@test.com");

        Heartbeat heartbeat = smtpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals(HeartbeatStatus.DOWN, heartbeat.getStatus());
        verify(emailService, times(1)).sendEmail(null, 25, SmtpUptimeCheckStrategy.EMAIL_FROM, "test@test.com");
    }
}
