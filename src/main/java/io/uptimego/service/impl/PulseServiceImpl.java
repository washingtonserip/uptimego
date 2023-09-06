package io.uptimego.service.impl;

import io.uptimego.model.Pulse;
import io.uptimego.model.User;
import io.uptimego.repository.PulseRepository;
import io.uptimego.repository.TargetRepository;
import io.uptimego.security.service.UserServiceImpl;
import io.uptimego.service.PulseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PulseServiceImpl implements PulseService {

    private PulseRepository repository;
    private final UserServiceImpl userService;

    public Pulse save(Pulse pulse) {
        return repository.save(pulse);
    }

    public List<Pulse> findByCurrentUser() {
        User user = userService.getCurrentUser();
        return repository.findByUser(user);
    }

}
