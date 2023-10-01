package io.uptimego.service.impl;

import io.uptimego.model.Pulse;
import io.uptimego.model.User;
import io.uptimego.repository.PulseRepository;
import io.uptimego.security.service.UserService;
import io.uptimego.service.PulseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PulseServiceImpl implements PulseService {

    private final PulseRepository repository;
    private final UserService userService;

    public Pulse save(Pulse pulse) {
        return repository.save(pulse);
    }

    public List<Pulse> findByCurrentUser() {
        User user = userService.getCurrentUser();
        return repository.findAllByUser(user);
    }

}
