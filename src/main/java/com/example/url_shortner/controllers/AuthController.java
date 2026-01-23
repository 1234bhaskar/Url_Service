package com.example.url_shortner.controllers;

import com.example.url_shortner.dto.LoginRequestDTO;
import com.example.url_shortner.dto.LoginResponseDTO;
import com.example.url_shortner.dto.SignUpRequestDTO;
import com.example.url_shortner.dto.SignUpResponseDTO;
import com.example.url_shortner.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginReqDTO) {
        return ResponseEntity.ok(authService.login(loginReqDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO signUpReqDTO) {
        return ResponseEntity.ok(authService.signup(signUpReqDTO));
    }
}
