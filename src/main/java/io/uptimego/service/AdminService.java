package io.uptimego.service;

import io.uptimego.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private HeartbeatRepository heartbeatRepository;

//    public Heartbeat createHeartbeat(Heartbeat heartbeat) {
//        // Your business logic here
//    }

    // Repeat for other CRUD operations
}
