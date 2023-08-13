package io.uptimego.batch.uptimecheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.batch.uptimecheck.impl.*;
import io.uptimego.model.*;
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
    void uptimeCheckStrategyHandler_httpStrategy() {
        uptimeCheckStrategyHandler_genericTest("HTTP", httpUptimeStrategy);
    }

    @Test
    void uptimeCheckStrategyHandler_headStrategy() {
        uptimeCheckStrategyHandler_genericTest("HEAD", headUptimeStrategy);
    }

    @Test
    void uptimeCheckStrategyHandler_tcpStrategy() {
        uptimeCheckStrategyHandler_genericTest("TCP", tcpUptimeStrategy);
    }

    @Test
    void uptimeCheckStrategyHandler_dnsStrategy() {
        uptimeCheckStrategyHandler_genericTest("DNS", dnsUptimeStrategy);
    }

    @Test
    void uptimeCheckStrategyHandler_smtpStrategy() {
        uptimeCheckStrategyHandler_genericTest("SMTP", smtpUptimeStrategy);
    }

    @Test
    void uptimeCheckStrategyHandler_pingStrategy() {
        uptimeCheckStrategyHandler_genericTest("PING", pingUptimeStrategy);
    }

    private void uptimeCheckStrategyHandler_genericTest(String pulseType, UptimeCheckStrategy strategy) {
        // Setup
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        mockConfig.setType(UptimeConfigType.valueOf(pulseType));
        Pulse expectedPulse = EntityTestFactory.createPulse(mockConfig, PulseStatus.UP, 50);
        when(strategy.getPulse(mockConfig)).thenReturn(expectedPulse);

        // Execute
        Pulse actualPulse = uptimeCheckStrategyHandler.execute(mockConfig);

        // Verify
        assertEquals(expectedPulse, actualPulse);
        verify(strategy, times(1)).getPulse(mockConfig);
    }

    @Test
    void uptimeCheckStrategyHandler_unsupportedStrategy() {
        // Setup
        UptimeConfig uptimeConfig = new UptimeConfig();
        uptimeConfig.setType(UptimeConfigType.UNKNOWN);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> uptimeCheckStrategyHandler.execute(uptimeConfig));
    }
}
