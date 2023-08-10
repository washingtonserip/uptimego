package io.uptimego.controller;

import io.uptimego.model.UptimeConfig;
import io.uptimego.service.UptimeConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uptime")
public class UptimeConfigController {

    private final UptimeConfigService service;

    @PostMapping
    public UptimeConfig create(@RequestBody UptimeConfig uptimeConfig) {
        return service.create(uptimeConfig);
    }

    @GetMapping("/{id}")
    public UptimeConfig findById(@PathVariable UUID id) {
        return service.findById(id).orElseThrow(() -> new RuntimeException("UptimeConfig não encontrado"));
    }

    @GetMapping
    public List<UptimeConfig> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public UptimeConfig update(@PathVariable UUID id, @RequestBody UptimeConfig uptimeConfig) {
        uptimeConfig.setId(id);
        return service.update(uptimeConfig);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
