package io.uptimego.service;

import io.uptimego.exceptions.UserNotFoundException;
import io.uptimego.model.UptimeConfig;
import io.uptimego.model.User;
import io.uptimego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Component
public class EntityChangesListener {

    @Autowired
    private UserRepository userRepository;

    @PreUpdate
    @PreRemove
    private void beforeUpdateOrDelete(UptimeConfig config) {
        String email = getCurrentUserEmail();
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!config.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("User does not have permission to write this record.");
        }
    }

    private String getCurrentUserEmail() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
