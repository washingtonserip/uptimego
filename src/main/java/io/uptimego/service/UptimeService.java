package io.uptimego.service;

import io.uptimego.domain.Status;
import io.uptimego.domain.Target;
import io.uptimego.repository.StatusRepository;
import io.uptimego.repository.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UptimeService {

    private final TargetRepository targetRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public UptimeService(TargetRepository targetRepository, StatusRepository statusRepository) {
        this.targetRepository = targetRepository;
        this.statusRepository = statusRepository;
    }

    public List<Target> getAllTargets() {
        return targetRepository.findAll();
    }

    public void saveStatus(Status status) {
        statusRepository.save(status);
    }
}
