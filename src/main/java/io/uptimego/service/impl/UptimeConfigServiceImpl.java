package io.uptimego.service.impl;

import io.uptimego.model.UptimeConfig;
import io.uptimego.model.User;
import io.uptimego.repository.UptimeConfigRepository;
import io.uptimego.security.service.UserServiceImpl;
import io.uptimego.service.UptimeConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UptimeConfigServiceImpl implements UptimeConfigService {

    private final UptimeConfigRepository repository;
    private final UserServiceImpl userService;

    @Override
    public UptimeConfig create(UptimeConfig uptimeConfig) {
        User user = userService.getCurrentUser();
        uptimeConfig.setUser(user);
        return repository.save(uptimeConfig);
    }

    @Override
    public Optional<UptimeConfig> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<UptimeConfig> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<UptimeConfig> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public UptimeConfig update(UptimeConfig uptimeConfig) {
        return repository.save(uptimeConfig);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}