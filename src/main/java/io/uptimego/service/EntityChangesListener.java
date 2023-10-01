package io.uptimego.service;

import io.uptimego.exceptions.UserNotFoundException;
import io.uptimego.model.Target;
import io.uptimego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

@Component
public class EntityChangesListener {

    @Autowired
    private UserRepository userRepository;

    @PreUpdate
    @PreRemove
    private void beforeUpdateOrDelete(Target config) {
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
