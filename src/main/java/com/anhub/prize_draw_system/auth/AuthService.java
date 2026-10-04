package com.anhub.prize_draw_system.auth;

import com.anhub.prize_draw_system.auth.dto.SignUpRequest;
import com.anhub.prize_draw_system.auth.dto.LoginRequest;
import com.anhub.prize_draw_system.auth.enumerated.UserRole;
import com.anhub.prize_draw_system.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public boolean signup(SignUpRequest signUpRequest) {

        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setRole(UserRole.STAFF);
        userRepository.save(user);

        return true;
    }

    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.")
        );

        return jwtService.generateToken(user);
    }

    public UUID getCurrentUserId() {

        String username = getCurrentUserUsername();
        User user = userRepository.findByUsername(username).orElseThrow();

        return user.getId();
    }

    public String getCurrentUserUsername() {

        String username;
        try {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated.");
        }

        return username;
    }
}
