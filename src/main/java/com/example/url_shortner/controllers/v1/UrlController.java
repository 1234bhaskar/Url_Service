package com.example.url_shortner.controllers.v1;

import com.example.url_shortner.dto.GetUrlDTO;
import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.models.Url;
import com.example.url_shortner.repositories.UrlRepository;
import com.example.url_shortner.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/url")
public class UrlController {

    private final UrlRepository urlRepository;
    private final UrlService urlService;
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
        String originalUrl = urlService.getOriginalUrl(shortURL);
        return ResponseEntity.status(HttpStatus.FOUND) //302 Redirect
                .location(URI.create(originalUrl))
                .build();
    }
}
