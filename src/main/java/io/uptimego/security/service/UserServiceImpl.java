package io.uptimego.security.service;

import io.uptimego.model.User;
import io.uptimego.enums.UserRole;
import io.uptimego.repository.UserRepository;
import io.uptimego.security.dto.AuthenticatedUserDto;
import io.uptimego.security.dto.RegistrationRequest;
import io.uptimego.security.dto.RegistrationResponse;
import io.uptimego.security.mapper.UserMapper;
import io.uptimego.service.SubscriptionService;
import io.uptimego.service.UserValidationService;
import io.uptimego.utils.GeneralMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

    private final UserRepository userRepository;

    private final SubscriptionService subscriptionService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    @Override
    public User findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public RegistrationResponse registration(RegistrationRequest registrationRequest) {

        userValidationService.validateUser(registrationRequest);

        final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);
        userRepository.save(user);

        user.setSubscription(subscriptionService.createBasicSubscription(user));

        final String username = registrationRequest.getEmail();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

        log.info("{} registered successfully!", username);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

        final User user = findByUsername(username);

        return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            User user = findByUsername(email);
            if (user != null) {
                return user;
            }
        }

        throw new RuntimeException("Usuário não encontrado");
    }
}