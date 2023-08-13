package io.uptimego.service.impl;

import io.uptimego.model.Target;
import io.uptimego.model.User;
import io.uptimego.repository.TargetRepository;
import io.uptimego.security.service.UserServiceImpl;
import io.uptimego.service.TargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TargetServiceImpl implements TargetService {

    private final TargetRepository repository;
    private final UserServiceImpl userService;

    @Override
    public Target create(Target target) {
        User user = userService.getCurrentUser();
        target.setUser(user);
        return repository.save(target);
    }

    @Override
    public Optional<Target> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Target> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Target> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Target update(Target target) {
        return repository.save(target);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
