package com.das.controllers;

import com.das.requests.JwtAuthRequest;
import com.das.responses.JwtAuthResponse;
import com.das.requests.RegisterRequest;
import com.das.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> register(
            @Valid @RequestBody RegisterRequest user
    ) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtAuthResponse> authenticate(
            @Valid @RequestBody JwtAuthRequest user
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }
}
