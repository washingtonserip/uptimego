package io.uptimego.processor.strategy;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.HeartbeatOptions;
import io.uptimego.model.Uptime;
import io.uptimego.service.SocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TcpUptimeStrategyTest {

    private TcpUptimeStrategy tcpUptimeStrategy;
    private SocketService socketService;

    @BeforeEach
    void setUp() {
        socketService = mock(SocketService.class);
        tcpUptimeStrategy = new TcpUptimeStrategy(socketService);
    }

    @Test
    void shouldReturnUptimeAsUpWhenSocketConnects() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("localhost");
        heartbeat.getOptions().setPort(80);

        Uptime uptime = tcpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("up", uptime.getStatus());
        verify(socketService, times(1)).connectSocket("localhost", 80);
    }

    @Test
    void shouldReturnUptimeAsDownWhenSocketCannotConnect() throws Exception {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setOptions(new HeartbeatOptions());
        heartbeat.getOptions().setHost("localhost");
        heartbeat.getOptions().setPort(80);

        doThrow(new Exception()).when(socketService).connectSocket("localhost", 80);

        Uptime uptime = tcpUptimeStrategy.checkUptime(heartbeat);

        assertEquals("down", uptime.getStatus());
        verify(socketService, times(1)).connectSocket("localhost", 80);
    }
}
