package io.uptimego.cron.targetcheck;

import io.uptimego.model.PlanSlug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TargetCheckSchedulerTest {

    @InjectMocks
    private TargetCheckScheduler targetCheckScheduler;

    @Mock
    private TargetCheckJob targetCheckJob;

    @Test
    public void triggerTargetCheckForBasicUsers_ShouldCallJob() throws Exception {
        targetCheckScheduler.triggerTargetCheckForBasicUsers();
        Mockito.verify(targetCheckJob).execute(PlanSlug.BASIC);
    }

    @Test
    public void triggerTargetCheckForBasicUsers_ShouldThrowRuntimeExceptionOnException() throws Exception {
        Mockito.doThrow(new Exception()).when(targetCheckJob).execute(PlanSlug.BASIC);
        assertThrows(RuntimeException.class, () -> targetCheckScheduler.triggerTargetCheckForBasicUsers());
    }

    @Test
    public void triggerTargetCheckForProUsers_ShouldCallJob() throws Exception {
        targetCheckScheduler.triggerTargetCheckForProUsers();
        Mockito.verify(targetCheckJob).execute(PlanSlug.PRO);
    }

    @Test
    public void triggerTargetCheckForProUsers_ShouldThrowRuntimeExceptionOnException() throws Exception {
        Mockito.doThrow(new Exception()).when(targetCheckJob).execute(PlanSlug.BASIC);
        assertThrows(RuntimeException.class, () -> targetCheckScheduler.triggerTargetCheckForProUsers());
    }
}
