package io.uptimego.controller;

import io.uptimego.model.Heartbeat;
import io.uptimego.service.AdminService;
import io.uptimego.service.HeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

//    @PostMapping
//    public Heartbeat createHeartbeat(@RequestBody Heartbeat heartbeat) {
//        // Your business logic here
//    }

    // Repeat for other CRUD operations
}
