package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.model.HeartbeatType;
import io.uptimego.processor.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UptimeProcessorTest {

    @Mock
    private HttpUptimeStrategy httpUptimeStrategy;

    @Mock
    private HeadUptimeStrategy headUptimeStrategy;

    @Mock
    private TcpUptimeStrategy tcpUptimeStrategy;

    @Mock
    private DnsUptimeStrategy dnsUptimeStrategy;

    @Mock
    private SmtpUptimeStrategy smtpUptimeStrategy;

    @Mock
    private SshUptimeStrategy sshUptimeStrategy;

    @Mock
    private PingUptimeStrategy pingUptimeStrategy;

    private UptimeProcessor uptimeProcessor;

    @BeforeEach
    void setUp() {
        when(httpUptimeStrategy.getType()).thenReturn("HTTP");
        when(headUptimeStrategy.getType()).thenReturn("HEAD");
        when(tcpUptimeStrategy.getType()).thenReturn("TCP");
        when(dnsUptimeStrategy.getType()).thenReturn("DNS");
        when(smtpUptimeStrategy.getType()).thenReturn("SMTP");
        when(sshUptimeStrategy.getType()).thenReturn("SSH");
        when(pingUptimeStrategy.getType()).thenReturn("PING");

        List<UptimeStrategy> uptimeStrategies = Arrays.asList(
                httpUptimeStrategy,
                headUptimeStrategy,
                tcpUptimeStrategy,
                dnsUptimeStrategy,
                smtpUptimeStrategy,
                sshUptimeStrategy,
                pingUptimeStrategy
        );
        uptimeProcessor = new UptimeProcessor(uptimeStrategies);
    }

    @Test
    void processUptime_httpStrategy() {
        processUptime_genericTest("HTTP", httpUptimeStrategy);
    }

    @Test
    void processUptime_headStrategy() {
        processUptime_genericTest("HEAD", headUptimeStrategy);
    }

    @Test
    void processUptime_tcpStrategy() {
        processUptime_genericTest("TCP", tcpUptimeStrategy);
    }

    @Test
    void processUptime_dnsStrategy() {
        processUptime_genericTest("DNS", dnsUptimeStrategy);
    }

    @Test
    void processUptime_smtpStrategy() {
        processUptime_genericTest("SMTP", smtpUptimeStrategy);
    }

    @Test
    void processUptime_sshStrategy() {
        processUptime_genericTest("SSH", sshUptimeStrategy);
    }

    @Test
    void processUptime_pingStrategy() {
        processUptime_genericTest("PING", pingUptimeStrategy);
    }

    private void processUptime_genericTest(String heartbeatType, UptimeStrategy strategy) {
        // Setup
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setType(HeartbeatType.valueOf(heartbeatType));
        Uptime expectedUptime = new Uptime();
        when(strategy.checkUptime(heartbeat)).thenReturn(expectedUptime);

        // Execute
        Uptime actualUptime = uptimeProcessor.processUptime(heartbeat);

        // Verify
        assertEquals(expectedUptime, actualUptime);
        verify(strategy, times(1)).checkUptime(heartbeat);
    }

    @Test
    void processUptime_unsupportedStrategy() {
        // Setup
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setType(HeartbeatType.UNKNOWN);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> uptimeProcessor.processUptime(heartbeat));
    }
}
