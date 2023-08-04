package io.uptimego.service;

import io.uptimego.repository.HeartbeatRepository;
import io.uptimego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final HeartbeatRepository heartbeatRepository;

    private final UserRepository userRepository;

    @Autowired
    public AdminService(HeartbeatRepository heartbeatRepository, UserRepository userRepository) {
        this.heartbeatRepository = heartbeatRepository;
        this.userRepository = userRepository;
    }

//    public Heartbeat createHeartbeat(Heartbeat heartbeat) {
//        // Your business logic here
//    }

    // Repeat for other CRUD operations
}
