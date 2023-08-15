package io.uptimego.cron.targetcheck.impl;

import io.uptimego.EntityTestFactory;
import io.uptimego.enums.PulseStatus;
import io.uptimego.enums.TargetType;
import io.uptimego.model.*;
import io.uptimego.service.HttpClientService;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeadTargetCheckJobStrategyTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private HeadTargetCheckStrategy headTargetCheckStrategy;

    private Target target;

    @BeforeEach
    public void setUp() {
        User user = EntityTestFactory.createUser();
        target = EntityTestFactory.createTarget(user, "https://uptimego.io", TargetType.HEAD);
    }

    @Test
    public void getPulse_Success() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(true);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Pulse pulse = headTargetCheckStrategy.getPulse(target);

        assertEquals(PulseStatus.UP, pulse.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void getPulse_Failure() throws Exception {
        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(false);
        when(httpClientService.executeHeadRequest(any())).thenReturn(response);

        Pulse pulse = headTargetCheckStrategy.getPulse(target);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }

    @Test
    public void getPulse_Exception() throws Exception {
        Exception error = new IOException();
        when(httpClientService.executeHeadRequest(any())).thenThrow(error);

        Pulse pulse = headTargetCheckStrategy.getPulse(target);

        assertEquals(PulseStatus.DOWN, pulse.getStatus());
        verify(httpClientService, times(1)).executeHeadRequest(any());
    }
}
