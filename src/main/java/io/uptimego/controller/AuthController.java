package io.uptimego.controller;

import io.uptimego.model.User;
import io.uptimego.model.UserRole;
import io.uptimego.repository.UserRepository;
import io.uptimego.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encryptedPassword);
        user.setRole(UserRole.USER);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String token) {
    }

    public record UserRegistrationRequest(String name, String email, String password) {
    }

}
