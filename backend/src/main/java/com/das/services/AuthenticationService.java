package com.das.services;

import com.das.dtos.UserLoginDTO;
import com.das.dtos.UserRegisterDTO;
import com.das.entities.Role;
import com.das.entities.User;
import com.das.repositories.UserRepository;
import com.das.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRegisterDTO userData) {
        User user = User.builder()
                .name(userData.getUsername())
                .email(userData.getEmail())
                .password(passwordEncoder.encode(userData.getPassword()))
                .roles(List.of(Role.EMPLOYEE))
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(UserLoginDTO userData) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userData.getEmail(),
                        userData.getPassword()
                )
        );

        User user = userRepository.findByEmail(userData.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
