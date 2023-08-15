package io.uptimego.cron.pulsecheck;

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
public class PulseCheckSchedulerTest {

    @InjectMocks
    private PulseCheckScheduler targetCheckScheduler;

    @Mock
    private PulseCheckJob pulseCheckJob;

    @Test
    public void triggerPulseCheck_ShouldCallJob() throws Exception {
        targetCheckScheduler.triggerPulseCheck();
        Mockito.verify(pulseCheckJob).execute();
    }

    @Test
    public void triggerPulseCheck_ShouldThrowRuntimeExceptionOnException() throws Exception {
        Mockito.doThrow(new Exception()).when(pulseCheckJob).execute();
        assertThrows(RuntimeException.class, () -> targetCheckScheduler.triggerPulseCheck());
    }
}
