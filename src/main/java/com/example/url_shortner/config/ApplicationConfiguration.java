package com.example.url_shortner.config;

import com.example.url_shortner.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService; // Fixed import
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Correct return type is UserDetailsService
        return username -> userRepository.findByEmail(username) // Use findByUsername or findByEmail
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        // Fix: Pass the passwordEncoder directly into the constructor
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
//
//        // Use setUserDetailsService instead of setUserDetailsPasswordService
//        authProvider.setUserDetailsService(userDetailsService());
//
//        return authProvider;
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        // 1. Use the default constructor (0 arguments)
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        // 2. Set the UserDetailsService (Finds the user)
//        authProvider.setUserDetailsService(userDetailsService());
//
//        // 3. Set the PasswordEncoder (Validates the password)
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
}