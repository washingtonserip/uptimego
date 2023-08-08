package io.uptimego.service;

import io.uptimego.repository.HeartbeatRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AdminService {
    private HeartbeatRepository heartbeatRepository;

//    public Heartbeat createHeartbeat(Heartbeat heartbeat) {
//        // Your business logic here
//    }

    // Repeat for other CRUD operations
}
