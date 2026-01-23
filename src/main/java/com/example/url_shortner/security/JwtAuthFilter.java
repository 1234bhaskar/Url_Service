package com.example.url_shortner.security;

import com.example.url_shortner.models.User;
import com.example.url_shortner.repositories.UserRepository;
import com.example.url_shortner.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          final String authorizationHeader = request.getHeader("Authorization");
          if(authorizationHeader ==null || !authorizationHeader.startsWith("Bearer ")){
              filterChain.doFilter(request,response);
              return;
          }

          String token = authorizationHeader.split("Bearer ")[1];
          String email= jwtService.extractUsername(token);

          if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
              User user =userRepository.findByEmail(email).orElseThrow();
              UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

              SecurityContextHolder.getContext().setAuthentication(authentication);
          }
          filterChain.doFilter(request,response);
    }


}
