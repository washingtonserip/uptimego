package io.uptimego.processor;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.Uptime;
import io.uptimego.service.HeartbeatService;
import io.uptimego.service.UptimeService;
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

public class HeartbeatProcessorTest {

    @Mock
    private HeartbeatService heartbeatService;

    @Mock
    private UptimeService uptimeService;

    @Mock
    private UptimeProcessor uptimeProcessor;

    @InjectMocks
    private HeartbeatProcessor heartbeatProcessor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void processHeartbeats_noHeartbeatsPageAvailable() {
        // Given
        Pageable pageable = PageRequest.of(0, HeartbeatProcessor.PAGE_SIZE);
        when(heartbeatService.findAll(pageable)).thenReturn(Page.empty());

        // When
        heartbeatProcessor.processHeartbeats();

        // Then
        verify(heartbeatService, times(1)).findAll(pageable);
        verifyNoMoreInteractions(heartbeatService, uptimeService, uptimeProcessor);
    }

    @Test
    public void processHeartbeats_singleHeartbeatSuccessfullyProcessed() {
        // Given
        Heartbeat heartbeat = new Heartbeat();
        Uptime uptime = new Uptime();
        Page<Heartbeat> page = new PageImpl<>(Collections.singletonList(heartbeat));

        when(heartbeatService.findAll(any(Pageable.class))).thenReturn(page);
        when(uptimeProcessor.processUptime(heartbeat)).thenReturn(uptime);

        // When
        heartbeatProcessor.processHeartbeats();

        // Then
        verify(uptimeProcessor, times(1)).processUptime(heartbeat);
        verify(uptimeService, times(1)).save(uptime);
    }

    @Test
    public void processHeartbeats_singleHeartbeatProcessingFails() {
        // Given
        Heartbeat heartbeat = new Heartbeat();
        Page<Heartbeat> page = new PageImpl<>(Collections.singletonList(heartbeat));

        when(heartbeatService.findAll(any(Pageable.class))).thenReturn(page);
        when(uptimeProcessor.processUptime(heartbeat)).thenThrow(new RuntimeException());

        // When
        heartbeatProcessor.processHeartbeats();

        // Then
        verify(uptimeProcessor, times(1)).processUptime(heartbeat);
        verifyNoInteractions(uptimeService);
    }
}
