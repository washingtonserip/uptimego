package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.model.UptimeConfigType;
import io.uptimego.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.SendFailedException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmtpHeartbeatStrategyTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SmtpHeartbeatStrategy smtpUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(UUID.randomUUID());
        uptimeConfig.setUserId(UUID.randomUUID());
        uptimeConfig.setType(UptimeConfigType.SMTP);
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setPort(25);
        uptimeConfig.getOptions().setEmailTo("test@test.com");
    }

    @Test
    void shouldReturnUptimeAsUpWhenEmailIsSent() throws Exception {
        uptimeConfig.getOptions().setHost("smtp.example.com");
        Heartbeat heartbeat = smtpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("up", heartbeat.getStatus());
        verify(emailService, times(1)).sendEmail("smtp.example.com", 25, SmtpHeartbeatStrategy.EMAIL_FROM, "test@test.com");
    }

    @Test
    void shouldReturnUptimeAsDownWhenEmailCannotBeSent() throws Exception {
        Exception error = new SendFailedException("No recipient addresses");
        doThrow(error).when(emailService).sendEmail(null, 25, SmtpHeartbeatStrategy.EMAIL_FROM, "test@test.com");

        Heartbeat heartbeat = smtpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals(error.getMessage(), heartbeat.getDetails().getStatusReason());
        verify(emailService, times(1)).sendEmail(null, 25, SmtpHeartbeatStrategy.EMAIL_FROM, "test@test.com");
    }
}
