package io.uptimego.service;

import io.uptimego.repository.HeartbeatRepository;
import io.uptimego.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final HeartbeatRepository heartbeatRepository;

    private final ClientRepository clientRepository;

    @Autowired
    public AdminService(HeartbeatRepository heartbeatRepository, ClientRepository clientRepository) {
        this.heartbeatRepository = heartbeatRepository;
        this.clientRepository = clientRepository;
    }

//    public Heartbeat createHeartbeat(Heartbeat heartbeat) {
//        // Your business logic here
//    }

    // Repeat for other CRUD operations
}
