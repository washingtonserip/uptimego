package io.uptimego.service;

import io.uptimego.model.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ChannelService {

    Channel create(Channel channel);

    Optional<Channel> findById(Long id);

    List<Channel> findAll();

    Page<Channel> findAll(Pageable pageable);

    Channel update(Channel channel);

    void delete(Long id);
}
