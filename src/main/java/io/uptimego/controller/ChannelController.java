package io.uptimego.controller;

import io.uptimego.model.Target;
import io.uptimego.service.TargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {

    private final TargetService service;

    @PostMapping
    public Target create(@RequestBody Target target) {
        return service.create(target);
    }

    @GetMapping("/{id}")
    public Target findById(@PathVariable Long id) {
        return service.findById(id).orElseThrow(() -> new RuntimeException("targets not found"));
    }

    @GetMapping
    public List<Target> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public Target update(@PathVariable Long id, @RequestBody Target target) {
        target.setId(id);
        return service.update(target);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
