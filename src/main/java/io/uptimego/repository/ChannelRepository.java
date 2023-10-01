package io.uptimego.repository;

import io.uptimego.model.Channel;
import io.uptimego.model.Target;
import io.uptimego.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    List<Channel> findByUser(User user);
}
