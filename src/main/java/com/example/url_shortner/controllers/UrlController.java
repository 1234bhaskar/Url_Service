package com.example.url_shortner.controllers;

import com.example.url_shortner.dto.GetUrlDTO;
import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private UrlService urlService;

    public UrlController( UrlService  urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String getHello(){
        System.out.println("Hello World");
        return  "Hello World";
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
