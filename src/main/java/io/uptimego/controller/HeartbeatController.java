package io.uptimego.controller;

import io.uptimego.service.HeartbeatService;
import io.uptimego.model.Heartbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/heartbeats")
public class HeartbeatController {
    private final HeartbeatService heartbeatService;

    @Autowired
    public HeartbeatController(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @GetMapping
    public Page<Heartbeat> getAllHeartbeats(Pageable pageable) {
        return heartbeatService.findAll(pageable);
    }

    @PostMapping
    public Heartbeat createHeartbeat(@RequestBody Heartbeat heartbeat) {
        return heartbeatService.save(heartbeat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Heartbeat> getHeartbeatById(@PathVariable Long id) {
        return heartbeatService.findById(id)
                .map(heartbeat -> ResponseEntity.ok().body(heartbeat))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Heartbeat> updateHeartbeat(@PathVariable Long id, @RequestBody Heartbeat heartbeat) {
        return heartbeatService.findById(id)
                .map(existingHeartbeat -> {
                    existingHeartbeat.setUrl(heartbeat.getUrl());
                    existingHeartbeat.setType(heartbeat.getType());
                    existingHeartbeat.setOptions(heartbeat.getOptions());
                    return ResponseEntity.ok().body(heartbeatService.save(existingHeartbeat));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHeartbeat(@PathVariable Long id) {
        return heartbeatService.findById(id)
                .map(existingHeartbeat -> {
                    heartbeatService.delete(existingHeartbeat);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
