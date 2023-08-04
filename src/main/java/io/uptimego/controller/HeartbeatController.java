package io.uptimego.controller;

import io.uptimego.model.Heartbeat;
import io.uptimego.service.HeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartbeats")
public class HeartbeatController {
    private final HeartbeatService heartbeatService;

    @Autowired
    public HeartbeatController(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

//    @PostMapping
//    public Heartbeat createHeartbeat(@RequestBody Heartbeat heartbeat) {
//        // Your business logic here
//    }

    // Repeat for other CRUD operations
}
