package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;
import io.uptimego.service.SshService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SshUptimeStrategyTest {

    private SshUptimeStrategy sshUptimeStrategy;
    private SshService sshService;

    @BeforeEach
    void setUp() {
        sshService = mock(SshService.class);
        sshUptimeStrategy = new SshUptimeStrategy(sshService);
    }

    @Test
    void shouldReturnUptimeAsUpWhenSshSessionIsEstablished() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("localhost");
        heartbeat.getOptions().setUsername("root");
        heartbeat.getOptions().setPassword("password");

        Uptime uptime = sshUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
        verify(sshService, times(1)).establishSshSession("root", "localhost", "password");
    }

    @Test
    void shouldReturnUptimeAsDownWhenSshSessionCannotBeEstablished() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("localhost");
        heartbeat.getOptions().setUsername("root");
        heartbeat.getOptions().setPassword("password");

        doThrow(new Exception()).when(sshService).establishSshSession("root", "localhost", "password");

        Uptime uptime = sshUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        verify(sshService, times(1)).establishSshSession("root", "localhost", "password");
    }
}
