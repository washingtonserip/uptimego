package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.service.HeartbeatService;
import io.uptimego.service.UptimeConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class HeartbeatCronjobTest {

    @Mock
    private UptimeConfigService uptimeConfigService;

    @Mock
    private HeartbeatService heartbeatService;

    @Mock
    private HeartbeatProcessor heartbeatProcessor;

    @InjectMocks
    private HeartbeatCronjob heartbeatCronjob;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void processHeartbeats_noHeartbeatsPageAvailable() {
        // Given
        Pageable pageable = PageRequest.of(0, HeartbeatCronjob.PAGE_SIZE);
        when(uptimeConfigService.findAll(pageable)).thenReturn(Page.empty());

        // When
        heartbeatCronjob.processHeartbeats();

        // Then
        verify(uptimeConfigService, times(1)).findAll(pageable);
        verifyNoMoreInteractions(uptimeConfigService, heartbeatService, heartbeatProcessor);
    }

    @Test
    public void processHeartbeats_singleHeartbeatSuccessfullyProcessed() {
        // Given
        UptimeConfig uptimeConfig = new UptimeConfig();
        Heartbeat heartbeat = new Heartbeat();
        Page<UptimeConfig> page = new PageImpl<>(Collections.singletonList(uptimeConfig));

        when(uptimeConfigService.findAll(any(Pageable.class))).thenReturn(page);
        when(heartbeatProcessor.execute(uptimeConfig)).thenReturn(heartbeat);

        // When
        heartbeatCronjob.processHeartbeats();

        // Then
        verify(heartbeatProcessor, times(1)).execute(uptimeConfig);
        verify(heartbeatService, times(1)).save(heartbeat);
    }

    @Test
    public void processHeartbeats_singleHeartbeatProcessingFails() {
        // Given
        UptimeConfig uptimeConfig = new UptimeConfig();
        Page<UptimeConfig> page = new PageImpl<>(Collections.singletonList(uptimeConfig));

        when(uptimeConfigService.findAll(any(Pageable.class))).thenReturn(page);
        when(heartbeatProcessor.execute(uptimeConfig)).thenThrow(new RuntimeException());

        // When
        heartbeatCronjob.processHeartbeats();

        // Then
        verify(heartbeatProcessor, times(1)).execute(uptimeConfig);
        verifyNoInteractions(heartbeatService);
    }
}
