package io.uptimego.service.impl;

import io.uptimego.model.User;
import io.uptimego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(username));
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return user;
    }
}
