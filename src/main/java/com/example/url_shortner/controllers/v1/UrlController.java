package com.example.url_shortner.controllers.v1;

import com.example.url_shortner.dto.GetUrlDTO;
import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.models.Url;
import com.example.url_shortner.repositories.UrlRepository;
import com.example.url_shortner.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/url")
public class UrlController {

    private final UrlRepository urlRepository;
    private UrlService urlService;

    public UrlController(UrlService  urlService, UrlRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    @GetMapping("/test")
    public String getHello(){
        return  "Health Check";
    }

    @PostMapping("/create")
    public ResponseEntity<?> postUrl(@RequestBody GetUrlDTO url, @AuthenticationPrincipal UserDetails userDetails) {
        ShortURLResponseDTO response =urlService.generateShortURLService(url.getOriginalUrl(),userDetails.getUsername());
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shortURL}")
    public ResponseEntity<?> getUrl(@PathVariable String shortURL){

        Url url =urlRepository.findByShortCode(shortURL)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));

        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT) // 302 Redirect
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }
}
