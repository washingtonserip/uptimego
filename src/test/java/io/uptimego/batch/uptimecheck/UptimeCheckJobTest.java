package io.uptimego.batch.uptimecheck;

import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
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
import static org.mockito.Mockito.when;

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
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Heartbeat mockHeartbeat1 = EntityTestFactory.createHeartbeat(mockConfig1, HeartbeatStatus.DOWN, 100);
        Heartbeat mockHeartbeat2 = EntityTestFactory.createHeartbeat(mockConfig2, HeartbeatStatus.UP, 50);
        List<UptimeConfig> mockConfigList = Arrays.asList(mockConfig1, mockConfig2);
        List<Heartbeat> mockHeartbeatList = Arrays.asList(mockHeartbeat1, mockHeartbeat2);

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
        User user = EntityTestFactory.createUser();
        UptimeConfig mockConfig1 = EntityTestFactory.createUptimeConfig(user, "https://uptimego.io");
        UptimeConfig mockConfig2 = EntityTestFactory.createUptimeConfig(user, "https://wpires.com.br");
        Heartbeat mockHeartbeat1 = EntityTestFactory.createHeartbeat(mockConfig1, HeartbeatStatus.DOWN, 100);
        Heartbeat mockHeartbeat2 = EntityTestFactory.createHeartbeat(mockConfig2, HeartbeatStatus.UP, 50);
        List<UptimeConfig> mockConfigList = Arrays.asList(mockConfig1, mockConfig2);
        List<Heartbeat> mockHeartbeatList = Arrays.asList(mockHeartbeat1, mockHeartbeat2);

        Page<UptimeConfig> singlePage = new PageImpl<>(mockConfigList);
        when(uptimeConfigRepository.findAll(any(Pageable.class)))
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

        when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(emptyPage);

        uptimeCheckJob.execute();

        Mockito.verify(uptimeCheckProcessor, Mockito.never()).process(anyList());
        Mockito.verify(heartbeatRepository, Mockito.never()).saveAll(anyList());
    }

    @Test
    public void execute_ShouldThrowException_WhenProcessorFails() throws Exception {
        List<UptimeConfig> mockConfigList = Arrays.asList(new UptimeConfig());
        Page<UptimeConfig> singlePage = new PageImpl<>(mockConfigList);

        when(uptimeConfigRepository.findAll(any(Pageable.class)))
                .thenReturn(singlePage);

        when(uptimeCheckProcessor.process(anyList()))
                .thenThrow(new RuntimeException("Processor error"));

        assertThrows(RuntimeException.class, () -> uptimeCheckJob.execute());
    }
}
