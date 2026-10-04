package com.anhub.prize_draw_system.auth;

import com.anhub.prize_draw_system.auth.dto.SignUpRequest;
import com.anhub.prize_draw_system.auth.dto.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        return authService.signup(signUpRequest) ?
                ResponseEntity.ok().body("User created successfully.") : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
