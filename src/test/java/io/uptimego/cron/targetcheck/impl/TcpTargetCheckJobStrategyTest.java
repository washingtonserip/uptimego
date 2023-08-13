package io.uptimego.cron.targetcheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
import io.uptimego.service.SocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TcpTargetCheckJobStrategyTest {

    @Mock
    private SocketService socketService;

    @InjectMocks
    private TcpTargetCheckStrategy tcpUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        uptimeConfig = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io", UptimeConfigType.TCP);
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setPort(80);
    }

    @Test
    void shouldReturnUptimeAsUpWhenSocketConnects() throws Exception {
        uptimeConfig.getOptions().setHost("localhost");
        Pulse pulse = tcpUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.UP, pulse.getStatus());
        verify(socketService, times(1)).connectSocket("localhost", 80);
    }

    @Test
    void shouldReturnUptimeAsDownWhenSocketCannotConnect() throws Exception {
        Exception error = new IllegalArgumentException("connect: The address can't be null");
        doThrow(error).when(socketService).connectSocket(null, 80);

        Pulse pulse = tcpUptimeStrategy.getPulse(uptimeConfig);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        verify(socketService, times(1)).connectSocket(null, 80);
    }
}
