package com.example.url_shortner.controllers.v1;

import com.example.url_shortner.dto.GetUrlDTO;
import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
public class UrlController {

    private UrlService urlService;

    public UrlController( UrlService  urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/test")
    public String getHello(){
        return  "Health Check";
    }

    @PostMapping("/")
    public ResponseEntity<?> postUrl(@RequestBody GetUrlDTO url) {
        ShortURLResponseDTO response =urlService.generateShortURLService(url.getOriginalUrl());
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
