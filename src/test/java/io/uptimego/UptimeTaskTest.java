package io.uptimego;

import io.uptimego.domain.Status;
import io.uptimego.domain.Target;
import io.uptimego.repository.StatusRepository;
import io.uptimego.repository.TargetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UptimeTaskTest {

    @Mock
    private TargetRepository targetRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private UptimeCheck uptimeCheck;

    @InjectMocks
    private UptimeTask uptimeTask;

    @Test
    public void shouldCheckUptimeForAvailableSite() {
        List<Target> targets = Collections.singletonList(new Target(1, "http://google.com"));

        Status status = new io.uptimego.Status(1, true);

        when(targetRepository.getTargetsBatch(100)).thenReturn(targets);
        when(uptimeCheck.checkTarget(anyInt(), anyString())).thenReturn(status);

        uptimeTask.checkUptime();

        verify(targetRepository, times(1)).getTargetsBatch(100);
        verify(uptimeCheck, times(1)).checkTarget(anyInt(), anyString());
        verify(statusRepository, times(1)).saveStatus(status);
    }

    @Test
    public void shouldCheckUptimeForUnavailableSite() {
        List<Target> targets = Collections.singletonList(new Target(1, "http://siteunavailable.com"));

        Status status = new Status(1, false);

        when(targetRepository.getTargetsBatch(100)).thenReturn(targets);
        when(uptimeCheck.checkTarget(anyInt(), anyString())).thenReturn(status);

        uptimeTask.checkUptime();

        verify(targetRepository, times(1)).getTargetsBatch(100);
        verify(uptimeCheck, times(1)).checkTarget(anyInt(), anyString());
        verify(statusRepository, times(1)).saveStatus(status);
    }

    @Test
    public void shouldCheckUptimeForAllTargets() {
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(1, "http://google.com"));
        targets.add(new Target(2, "http://amazon.com"));

        Status status = new Status(1, true);

        when(targetRepository.getTargetsBatch(100)).thenReturn(targets);
        when(uptimeCheck.checkTarget(anyInt(), anyString())).thenReturn(status);

        uptimeTask.checkUptime();

        verify(targetRepository, times(1)).getTargetsBatch(100);
        verify(uptimeCheck, times(targets.size())).checkTarget(anyInt(), anyString());
        verify(statusRepository, times(targets.size())).saveStatus(status);
    }

    @Test
    public void shouldHandleExceptionWhenGettingTargetsFromDatabase() {
        when(targetRepository.getTargetsBatch(100)).thenThrow(RuntimeException.class);

        uptimeTask.checkUptime();

        verify(targetRepository, times(1)).getTargetsBatch(100);
        verify(uptimeCheck, never()).checkTarget(anyInt(), anyString());
        verify(statusRepository, never()).saveStatus(any());
    }

    @Test
    public void shouldHandleExceptionWhenSavingStatusInDatabase() {
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(1, "http://google.com"));

        Status status = new Status(1, true);

        when(targetRepository.getTargetsBatch(100)).thenReturn(targets);
        when(uptimeCheck.checkTarget(anyInt(), anyString())).thenReturn(status);
        doThrow(RuntimeException.class).when(statusRepository).saveStatus(any());

        uptimeTask.checkUptime();

        verify(targetRepository, times(1)).getTargetsBatch(100);
        verify(uptimeCheck, times(1)).checkTarget(anyInt(), anyString());
        verify(statusRepository, times(1)).saveStatus(status);
    }
}
