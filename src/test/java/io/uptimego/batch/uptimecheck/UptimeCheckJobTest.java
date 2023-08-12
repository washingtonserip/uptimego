package io.uptimego.batch.uptimecheck;

import io.uptimego.model.Heartbeat;
import io.uptimego.model.UptimeConfig;
import io.uptimego.repository.HeartbeatRepository;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UptimeCheckJobTest {

    @InjectMocks
    private UptimeCheckJob uptimeCheckJob;

    @Mock
    private UptimeConfigRepository uptimeConfigRepository;

    @Mock
    private UptimeCheckBatchProcessor uptimeCheckProcessor;

    @Mock
    private HeartbeatRepository heartbeatRepository;

    @Test
    public void execute_ShouldProcessUntilLastPage() throws Exception {
        UptimeConfig mockConfig1 = new UptimeConfig();
        mockConfig1.setId(1L);
        UptimeConfig mockConfig2 = new UptimeConfig();
        mockConfig2.setId(2L);
        List<UptimeConfig> mockConfigList = Arrays.asList(mockConfig1, mockConfig2);

        Heartbeat heartbeat1 = new Heartbeat();
        heartbeat1.setId(1L);
        Heartbeat heartbeat2 = new Heartbeat();
        heartbeat2.setId(2L);
        List<Heartbeat> mockHeartbeatList = Arrays.asList(heartbeat1, heartbeat2);

        // Pages (has next)
        Page<UptimeConfig> firstPage = new PageImpl<>(mockConfigList, PageRequest.of(0, 10), 20);
        Page<UptimeConfig> secondPage = new PageImpl<>(mockConfigList, PageRequest.of(1, 10), 20);

        doReturn(firstPage).when(uptimeConfigRepository).findAll(eq(PageRequest.of(0, 10)));
        doReturn(secondPage).when(uptimeConfigRepository).findAll(eq(PageRequest.of(1, 10)));
        doReturn(mockHeartbeatList).when(uptimeCheckProcessor).process(anyList());

        uptimeCheckJob.execute();

        Mockito.verify(uptimeCheckProcessor, Mockito.times(2)).process(anyList());
        Mockito.verify(heartbeatRepository, Mockito.times(2)).saveAll(anyList());
    }

    @Test
    public void execute_ShouldProcessSinglePageOnly_WhenOnlyOnePageExists() throws Exception {
        UptimeConfig mockConfig1 = new UptimeConfig();
        mockConfig1.setId(1L);
        UptimeConfig mockConfig2 = new UptimeConfig();
        mockConfig2.setId(2L);
        List<UptimeConfig> mockConfigList = Arrays.asList(mockConfig1, mockConfig2);

        Heartbeat heartbeat1 = new Heartbeat();
        heartbeat1.setId(1L);
        Heartbeat heartbeat2 = new Heartbeat();
        heartbeat2.setId(2L);
        List<Heartbeat> mockHeartbeatList = Arrays.asList(heartbeat1, heartbeat2);

        Page<UptimeConfig> singlePage = new PageImpl<>(mockConfigList);

        Mockito.when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(singlePage);

        doReturn(mockHeartbeatList).when(uptimeCheckProcessor).process(eq(mockConfigList));

        uptimeCheckJob.execute();

        Mockito.verify(uptimeConfigRepository, Mockito.times(1)).findAll(any(Pageable.class));
        Mockito.verify(heartbeatRepository).saveAll(eq(mockHeartbeatList));
    }

    @Test
    public void execute_ShouldHandleEmptyPageGracefully() throws Exception {
        // Empty page
        Page<UptimeConfig> emptyPage = Page.empty();

        Mockito.when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(emptyPage);

        uptimeCheckJob.execute();

        Mockito.verify(uptimeCheckProcessor, Mockito.never()).process(anyList());
        Mockito.verify(heartbeatRepository, Mockito.never()).saveAll(anyList());
    }

    @Test
    public void execute_ShouldThrowException_WhenProcessorFails() throws Exception {
        List<UptimeConfig> mockConfigList = Arrays.asList(new UptimeConfig());
        Page<UptimeConfig> singlePage = new PageImpl<>(mockConfigList);

        Mockito.when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(singlePage);

        Mockito.when(uptimeCheckProcessor.process(anyList()))
                .thenThrow(new RuntimeException("Processor error"));

        assertThrows(RuntimeException.class, () -> uptimeCheckJob.execute());
    }
}
