package com.example.url_shortner.services;

import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.dto.UniqueIdResponseDTO;
import com.example.url_shortner.models.Url;
import com.example.url_shortner.models.User;
import com.example.url_shortner.repositories.UrlRepository;
import com.example.url_shortner.repositories.UserRepository;
import com.example.url_shortner.utils.UrlUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UrlService {
    private final WebClient webClient;
    private final UrlUtil urlUtil;
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;

    public UrlService(WebClient webClient, UrlUtil urlUtil, UrlRepository urlRepository, UserRepository userRepository) {
    this.webClient=webClient;
    this.urlUtil=urlUtil;
    this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    public ShortURLResponseDTO generateShortURLService(String originalUrl,String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UniqueIdResponseDTO response=webClient.get()
                .uri("/api/getId")
                .retrieve()
                .bodyToMono(UniqueIdResponseDTO.class)
                .block();

        if(response==null){
            return ShortURLResponseDTO.builder().shortURL("").url(originalUrl).status("Failed").build();
        }
        if(response.getRandomID()==-1){
            return ShortURLResponseDTO.builder().shortURL("").url(originalUrl).status("Failed").build();
        }
        String code = urlUtil.IDToURLGenerator(response.getRandomID());
        Url urlObj=Url
                .builder()
                .user(user)
                .originalUrl(originalUrl)
                .shortCode(code)
                .clickCount(0L)
                .build();

        Url url= urlRepository.save(urlObj);

        return ShortURLResponseDTO
                .builder()
                .url(url.getOriginalUrl())
                .shortURL(url.getShortCode())
                .status(response.getStatus())
                .username(user.getUsername())
                .build();
    }

}
