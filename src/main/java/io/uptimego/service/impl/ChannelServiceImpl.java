package io.uptimego.service.impl;

import io.uptimego.model.Channel;
import io.uptimego.model.User;
import io.uptimego.repository.ChannelRepository;
import io.uptimego.security.service.UserService;
import io.uptimego.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository repository;
    private final UserService userService;

    @Override
    public Channel create(Channel channel) {
        User user = userService.getCurrentUser();
        channel.setUser(user);
        return repository.save(channel);
    }

    @Override
    public Optional<Channel> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        User user = userService.getCurrentUser();
        return repository.findByUser(user);
    }

    @Override
    public Page<Channel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Channel update(Channel channel) {
        User currentUser = userService.getCurrentUser();
        Optional<Channel> optionalChannel = findById(channel.getId());

        if (optionalChannel.isPresent() && optionalChannel.get().getUser().equals(currentUser)) {
            return repository.save(channel);
        } else {
            throw new SecurityException("User not authorized to update this channel");
        }
    }

    @Override
    public void delete(Long id) {
        User currentUser = userService.getCurrentUser();
        Optional<Channel> optionalTarget = findById(id);

        if (optionalTarget.isPresent() && optionalTarget.get().getUser().equals(currentUser)) {
            repository.deleteById(id);
        } else {
            throw new SecurityException("User not authorized to delete this channel");
        }
    }
}
