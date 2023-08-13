package io.uptimego.batch.uptimecheck;

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
public class UptimeCheckSchedulerTest {

    @InjectMocks
    private UptimeCheckScheduler uptimeCheckScheduler;

    @Mock
    private UptimeCheckJob uptimeCheckJob;

    @Test
    public void triggerUptimeCheck_ShouldCallJob() throws Exception {
        uptimeCheckScheduler.triggerUptimeCheck();
        Mockito.verify(uptimeCheckJob).execute(PlanSlug.BASIC);
    }

    @Test
    public void triggerUptimeCheck_ShouldThrowRuntimeExceptionOnException() throws Exception {
        Mockito.doThrow(new Exception()).when(uptimeCheckJob).execute(PlanSlug.BASIC);
        assertThrows(RuntimeException.class, () -> uptimeCheckScheduler.triggerUptimeCheck());
    }
}
