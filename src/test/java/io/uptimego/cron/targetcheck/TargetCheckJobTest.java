package io.uptimego.cron.targetcheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
import io.uptimego.repository.PulseRepository;
import io.uptimego.repository.UptimeConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TargetCheckJobTest {

    @InjectMocks
    private TargetCheckJob targetCheckJob;

    @Mock
    private UptimeConfigRepository uptimeConfigRepository;

    @Mock
    private TargetCheckProcessor targetCheckProcessor;

    @Mock
    private PulseRepository pulseRepository;

    @Test
    public void execute_ShouldProcessUntilLastPage() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Pulse mockPulse1 = EntityTestFactory.createPulse(mockConfig1, PulseStatus.DOWN, 100);
        Pulse mockPulse2 = EntityTestFactory.createPulse(mockConfig2, PulseStatus.UP, 50);
        List<UptimeConfig> mockConfigList = Arrays.asList(mockConfig1, mockConfig2);
        List<Pulse> mockPulseList = Arrays.asList(mockPulse1, mockPulse2);

        // Pages (has next)
        Page<UptimeConfig> firstPage = new PageImpl<>(mockConfigList, PageRequest.of(0, 10), 20);
        Page<UptimeConfig> secondPage = new PageImpl<>(mockConfigList, PageRequest.of(1, 10), 20);

        doReturn(firstPage).when(uptimeConfigRepository).findAll(eq(PageRequest.of(0, 10)));
        doReturn(secondPage).when(uptimeConfigRepository).findAll(eq(PageRequest.of(1, 10)));
        doReturn(mockPulseList).when(targetCheckProcessor).process(anyList());

        targetCheckJob.execute(PlanSlug.BASIC);

        Mockito.verify(targetCheckProcessor, Mockito.times(2)).process(anyList());
        Mockito.verify(pulseRepository, Mockito.times(2)).saveAll(anyList());
    }

    @Test
    public void execute_ShouldProcessSinglePageOnly_WhenOnlyOnePageExists() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Pulse mockPulse1 = EntityTestFactory.createPulse(mockConfig1, PulseStatus.DOWN, 100);
        Pulse mockPulse2 = EntityTestFactory.createPulse(mockConfig2, PulseStatus.UP, 50);
        List<UptimeConfig> mockConfigList = Arrays.asList(mockConfig1, mockConfig2);
        List<Pulse> mockPulseList = Arrays.asList(mockPulse1, mockPulse2);

        Page<UptimeConfig> singlePage = new PageImpl<>(mockConfigList);
        when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(singlePage);

        doReturn(mockPulseList).when(targetCheckProcessor).process(eq(mockConfigList));

        targetCheckJob.execute(PlanSlug.BASIC);

        Mockito.verify(uptimeConfigRepository, Mockito.times(1)).findAll(any(Pageable.class));
        Mockito.verify(pulseRepository).saveAll(eq(mockPulseList));
    }

    @Test
    public void execute_ShouldHandleEmptyPageGracefully() throws Exception {
        // Empty page
        Page<UptimeConfig> emptyPage = Page.empty();

        when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(emptyPage);

        targetCheckJob.execute(PlanSlug.BASIC);

        Mockito.verify(targetCheckProcessor, Mockito.never()).process(anyList());
        Mockito.verify(pulseRepository, Mockito.never()).saveAll(anyList());
    }

    @Test
    public void execute_ShouldThrowException_WhenProcessorFails() throws Exception {
        List<UptimeConfig> mockConfigList = Arrays.asList(new UptimeConfig());
        Page<UptimeConfig> singlePage = new PageImpl<>(mockConfigList);

        when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(singlePage);

        when(targetCheckProcessor.process(anyList()))
                .thenThrow(new RuntimeException("Processor error"));

        assertThrows(RuntimeException.class, () -> targetCheckJob.execute(PlanSlug.BASIC));
    }
}
