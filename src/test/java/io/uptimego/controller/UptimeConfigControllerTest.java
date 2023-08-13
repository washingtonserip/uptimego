package io.uptimego.controller;

import io.hypersistence.tsid.TSID;
import io.uptimego.EntityTestFactory;
import io.uptimego.model.*;
import io.uptimego.service.UptimeConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UptimeConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UptimeConfigService service;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testCreate() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig config = EntityTestFactory.createUptimeConfig(user, "https://example.com");
        when(service.create(any())).thenReturn(config);

        mockMvc.perform(post("/uptime")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com"));

        verify(service, times(1)).create(any());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testFindById() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig config = EntityTestFactory.createUptimeConfig(user, "https://example.com");
        when(service.findById(config.getId())).thenReturn(Optional.of(config));

        mockMvc.perform(get("/uptime/" + config.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com"));

        verify(service, times(1)).findById(config.getId());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testFindAll() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig config = EntityTestFactory.createUptimeConfig(user, "https://example.com");
        when(service.findAll()).thenReturn(Collections.singletonList(config));

        mockMvc.perform(get("/uptime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("https://example.com"));

        verify(service, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testUpdate() throws Exception {
        User user = EntityTestFactory.createUser();
        UptimeConfig config = EntityTestFactory.createUptimeConfig(user, "https://updated.com");
        when(service.update(any())).thenReturn(config);

        mockMvc.perform(put("/uptime/" + config.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://updated.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://updated.com"));

        verify(service, times(1)).update(any());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testDelete() throws Exception {
        long id = TSID.Factory.getTsid().toLong();

        mockMvc.perform(delete("/uptime/" + id))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(id);
    }
}
