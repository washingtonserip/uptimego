package io.uptimego.processor.strategy;

import com.jcraft.jsch.JSchException;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigOptions;
import io.uptimego.model.UptimeConfigType;
import io.uptimego.service.SshService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SshHeartbeatStrategyTest {

    @Mock
    private SshService sshService;

    @InjectMocks
    private SshHeartbeatStrategy sshUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(UUID.randomUUID());
        uptimeConfig.setUserId(UUID.randomUUID());
        uptimeConfig.setType(UptimeConfigType.SSH);
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setHost("localhost");
        uptimeConfig.getOptions().setUsername("root");
        uptimeConfig.getOptions().setPassword("password");
    }

    @Test
    void shouldReturnUptimeAsUpWhenSshSessionIsEstablished() throws Exception {
        Heartbeat heartbeat = sshUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("up", heartbeat.getStatus());
        verify(sshService, times(1)).establishSshSession("root", "localhost", "password");
    }

    @Test
    void shouldReturnUptimeAsDownWhenSshSessionCannotBeEstablished() throws Exception {
        Exception error = new JSchException("session is already connected");
        doThrow(error).when(sshService).establishSshSession("root", "localhost", "password");

        Heartbeat heartbeat = sshUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals(error.getMessage(), heartbeat.getDetails().getStatusReason());
        verify(sshService, times(1)).establishSshSession("root", "localhost", "password");
    }
}
