package com.das.authentication;

import com.das.authentication.jwt.JwtService;
import com.das.users.entities.UserRole;
import com.das.users.entities.User;
import com.das.common.exceptions.EmailNotAvailableException;
import com.das.users.UserRepository;
import com.das.users.requests.UserRegisterRequest;
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

    public AuthenticationResponse register(UserRegisterRequest userData) {
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new EmailNotAvailableException(userData.getEmail());
        }

        User user = User.builder()
                .name(userData.getUsername())
                .email(userData.getEmail())
                .password(passwordEncoder.encode(userData.getPassword()))
                .roles(List.of(UserRole.EMPLOYEE))
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest userData) {
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
