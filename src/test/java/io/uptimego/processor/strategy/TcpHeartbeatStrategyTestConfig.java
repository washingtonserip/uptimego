package io.uptimego.processor.strategy;

import io.uptimego.model.*;
import io.uptimego.service.SocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.UnknownHostException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TcpHeartbeatStrategyTestConfig {

    @Mock
    private SocketService socketService;

    @InjectMocks
    private TcpHeartbeatStrategy tcpUptimeStrategy;

    private UptimeConfig uptimeConfig;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(UUID.randomUUID());
        uptimeConfig = new UptimeConfig();
        uptimeConfig.setId(UUID.randomUUID());
        uptimeConfig.setUser(user);
        uptimeConfig.setType(UptimeConfigType.TCP);
        uptimeConfig.setOptions(new UptimeConfigOptions());
        uptimeConfig.getOptions().setPort(80);
    }

    @Test
    void shouldReturnUptimeAsUpWhenSocketConnects() throws Exception {
        uptimeConfig.getOptions().setHost("localhost");
        Heartbeat heartbeat = tcpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("up", heartbeat.getStatus());
        verify(socketService, times(1)).connectSocket("localhost", 80);
    }

    @Test
    void shouldReturnUptimeAsDownWhenSocketCannotConnect() throws Exception {
        Exception error = new IllegalArgumentException("connect: The address can't be null");;
        doThrow(error).when(socketService).connectSocket(null, 80);

        Heartbeat heartbeat = tcpUptimeStrategy.getHeartbeat(uptimeConfig);

        assertEquals("down", heartbeat.getStatus());
        assertEquals(error.getMessage(), heartbeat.getDetails().getStatusReason());
        verify(socketService, times(1)).connectSocket(null, 80);
    }
}
