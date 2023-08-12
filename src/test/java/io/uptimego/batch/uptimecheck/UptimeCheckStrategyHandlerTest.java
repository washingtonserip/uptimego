package io.uptimego.batch.uptimecheck;

import io.uptimego.batch.uptimecheck.impl.*;
import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.UptimeConfigType;
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
class UptimeCheckStrategyHandlerTest {

    @Mock
    private HttpUptimeCheckStrategy httpUptimeStrategy;

    @Mock
    private HeadUptimeCheckStrategy headUptimeStrategy;

    @Mock
    private TcpUptimeCheckStrategy tcpUptimeStrategy;

    @Mock
    private DnsUptimeCheckStrategy dnsUptimeStrategy;

    @Mock
    private SmtpUptimeCheckStrategy smtpUptimeStrategy;

    @Mock
    private PingUptimeCheckStrategy pingUptimeStrategy;

    private UptimeCheckStrategyHandler uptimeCheckStrategyHandler;

    @BeforeEach
    void setUp() {
        when(httpUptimeStrategy.getType()).thenReturn("HTTP");
        when(headUptimeStrategy.getType()).thenReturn("HEAD");
        when(tcpUptimeStrategy.getType()).thenReturn("TCP");
        when(dnsUptimeStrategy.getType()).thenReturn("DNS");
        when(smtpUptimeStrategy.getType()).thenReturn("SMTP");
        when(pingUptimeStrategy.getType()).thenReturn("PING");

        List<UptimeCheckStrategy> uptimeStrategies = Arrays.asList(
                httpUptimeStrategy,
                headUptimeStrategy,
                tcpUptimeStrategy,
                dnsUptimeStrategy,
                smtpUptimeStrategy,
                pingUptimeStrategy
        );
        uptimeCheckStrategyHandler = new UptimeCheckStrategyHandler(uptimeStrategies);
    }

    @Test
    void heartbeatProcessor_httpStrategy() {
        heartbeatProcessor_genericTest("HTTP", httpUptimeStrategy);
    }

    @Test
    void heartbeatProcessor_headStrategy() {
        heartbeatProcessor_genericTest("HEAD", headUptimeStrategy);
    }

    @Test
    void heartbeatProcessor_tcpStrategy() {
        heartbeatProcessor_genericTest("TCP", tcpUptimeStrategy);
    }

    @Test
    void heartbeatProcessor_dnsStrategy() {
        heartbeatProcessor_genericTest("DNS", dnsUptimeStrategy);
    }

    @Test
    void heartbeatProcessor_smtpStrategy() {
        heartbeatProcessor_genericTest("SMTP", smtpUptimeStrategy);
    }

    @Test
    void heartbeatProcessor_pingStrategy() {
        heartbeatProcessor_genericTest("PING", pingUptimeStrategy);
    }

    private void heartbeatProcessor_genericTest(String heartbeatType, UptimeCheckStrategy strategy) {
        // Setup
        UptimeConfig uptimeConfig = new UptimeConfig();
        uptimeConfig.setType(UptimeConfigType.valueOf(heartbeatType));
        Heartbeat expectedHeartbeat = new Heartbeat();
        when(strategy.getHeartbeat(uptimeConfig)).thenReturn(expectedHeartbeat);

        // Execute
        Heartbeat actualHeartbeat = uptimeCheckStrategyHandler.execute(uptimeConfig);

        // Verify
        assertEquals(expectedHeartbeat, actualHeartbeat);
        verify(strategy, times(1)).getHeartbeat(uptimeConfig);
    }

    @Test
    void heartbeatProcessor_unsupportedStrategy() {
        // Setup
        UptimeConfig uptimeConfig = new UptimeConfig();
        uptimeConfig.setType(UptimeConfigType.UNKNOWN);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> uptimeCheckStrategyHandler.execute(uptimeConfig));
    }
}
