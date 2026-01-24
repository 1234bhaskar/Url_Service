package com.example.url_shortner.services;

import com.example.url_shortner.dto.LoginRequestDTO;
import com.example.url_shortner.dto.LoginResponseDTO;
import com.example.url_shortner.dto.SignUpRequestDTO;
import com.example.url_shortner.dto.SignUpResponseDTO;
import com.example.url_shortner.models.User;
import com.example.url_shortner.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginResquestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginResquestDTO.getEmail(), loginResquestDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(Objects.requireNonNull(user).getUuid(),token,user.getEmail());
    }

    public SignUpResponseDTO signup(SignUpRequestDTO signUpRequestDTO) {

        User user =userRepository.findByEmail(signUpRequestDTO.getEmail()).orElse(null);

        if(user !=null) throw  new IllegalArgumentException("User already exists");

        user=userRepository.save(User.builder()
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .username(signUpRequestDTO.getUserName())
                .build()
        );

        return new SignUpResponseDTO(user.getId(),user.getUsername());
    }
}
