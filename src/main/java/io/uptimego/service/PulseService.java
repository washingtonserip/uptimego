package io.uptimego.service;

import io.uptimego.model.Pulse;
import io.uptimego.repository.PulseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PulseService {
    @Autowired
    private PulseRepository pulseRepository;

    public Pulse save(Pulse pulse) {
        return pulseRepository.save(pulse);
    }

}
