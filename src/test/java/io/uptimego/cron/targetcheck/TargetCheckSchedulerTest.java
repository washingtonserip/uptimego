package io.uptimego.cron.targetcheck;

import io.uptimego.model.PlanSlug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TargetCheckSchedulerTest {

    @InjectMocks
    private TargetCheckScheduler targetCheckScheduler;

    @Mock
    private TargetCheckJob targetCheckJob;

    @Test
    public void triggerTargetCheck_ShouldCallJob() throws Exception {
        targetCheckScheduler.triggerTargetCheck();
        Mockito.verify(targetCheckJob).execute(PlanSlug.BASIC);
    }

    @Test
    public void triggerTargetCheck_ShouldThrowRuntimeExceptionOnException() throws Exception {
        Mockito.doThrow(new Exception()).when(targetCheckJob).execute(PlanSlug.BASIC);
        assertThrows(RuntimeException.class, () -> targetCheckScheduler.triggerTargetCheck());
    }
}
