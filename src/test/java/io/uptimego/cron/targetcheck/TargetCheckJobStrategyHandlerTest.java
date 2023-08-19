package io.uptimego.cron.targetcheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.cron.targetcheck.impl.*;
import io.uptimego.enums.PulseStatus;
import io.uptimego.enums.TargetType;
import io.uptimego.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TargetCheckJobStrategyHandlerTest {

    @Mock
    private HttpTargetCheckStrategy httpUptimeStrategy;

    @Mock
    private HeadTargetCheckStrategy headUptimeStrategy;

    @Mock
    private TcpTargetCheckStrategy tcpUptimeStrategy;

    @Mock
    private DnsTargetCheckStrategy dnsUptimeStrategy;

    @Mock
    private PingTargetCheckStrategy pingUptimeStrategy;

    private TargetCheckStrategyHandler targetCheckStrategyHandler;

    @BeforeEach
    void setUp() {
        when(httpUptimeStrategy.getType()).thenReturn("HTTP");
        when(headUptimeStrategy.getType()).thenReturn("HEAD");
        when(tcpUptimeStrategy.getType()).thenReturn("TCP");
        when(dnsUptimeStrategy.getType()).thenReturn("DNS");
        when(pingUptimeStrategy.getType()).thenReturn("PING");

        List<TargetCheckStrategy> uptimeStrategies = Arrays.asList(
                httpUptimeStrategy,
                headUptimeStrategy,
                tcpUptimeStrategy,
                dnsUptimeStrategy,
                pingUptimeStrategy
        );
        targetCheckStrategyHandler = new TargetCheckStrategyHandler(uptimeStrategies);
    }

    @Test
    void targetCheckStrategyHandler_httpStrategy() {
        targetCheckStrategyHandler_genericTest("HTTP", httpUptimeStrategy);
    }

    @Test
    void targetCheckStrategyHandler_headStrategy() {
        targetCheckStrategyHandler_genericTest("HEAD", headUptimeStrategy);
    }

    @Test
    void targetCheckStrategyHandler_tcpStrategy() {
        targetCheckStrategyHandler_genericTest("TCP", tcpUptimeStrategy);
    }

    @Test
    void targetCheckStrategyHandler_dnsStrategy() {
        targetCheckStrategyHandler_genericTest("DNS", dnsUptimeStrategy);
    }

    @Test
    void targetCheckStrategyHandler_pingStrategy() {
        targetCheckStrategyHandler_genericTest("PING", pingUptimeStrategy);
    }

    private void targetCheckStrategyHandler_genericTest(String pulseType, TargetCheckStrategy strategy) {
        // Setup
        User user = EntityTestFactory.buildUser();
        Target mockConfig = EntityTestFactory.buildTarget(user, "https://uptimego.io");
        mockConfig.setType(TargetType.valueOf(pulseType));
        Pulse expectedPulse = EntityTestFactory.buildPulse(mockConfig, PulseStatus.UP, 50);
        when(strategy.getPulse(mockConfig)).thenReturn(expectedPulse);

        // Execute
        Pulse actualPulse = targetCheckStrategyHandler.execute(mockConfig);

        // Verify
        assertEquals(expectedPulse, actualPulse);
        verify(strategy, times(1)).getPulse(mockConfig);
    }

    @Test
    void targetCheckStrategyHandler_unsupportedStrategy() {
        // Setup
        Target target = new Target();
        target.setType(TargetType.UNKNOWN);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> targetCheckStrategyHandler.execute(target));
    }
}
