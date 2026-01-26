package com.example.url_shortner.services;

import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.dto.UniqueIdResponseDTO;
import com.example.url_shortner.models.Url;
import com.example.url_shortner.models.User;
import com.example.url_shortner.repositories.UrlRepository;
import com.example.url_shortner.repositories.UserRepository;
import com.example.url_shortner.utils.UrlUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Service
public class UrlService {
    private final WebClient webClient;
    private final UrlUtil urlUtil;
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public UrlService(WebClient webClient, UrlUtil urlUtil, UrlRepository urlRepository, UserRepository userRepository) {
    this.webClient=webClient;
    this.urlUtil=urlUtil;
    this.urlRepository = urlRepository;
    this.userRepository = userRepository;
    }

    @Transactional
    public ShortURLResponseDTO generateShortURLService(String originalUrl,String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UniqueIdResponseDTO response=webClient.get()
                .uri("/api/getId")
                .retrieve()
                .bodyToMono(UniqueIdResponseDTO.class)
                .block();

        if (response == null || response.getRandomID() == -1) {
            return ShortURLResponseDTO.builder().status("Failed").url(originalUrl).build();
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

            redisTemplate.opsForValue().set(
                    url.getShortCode(),
                    url.getOriginalUrl(),
                    1, TimeUnit.HOURS
            );

        return ShortURLResponseDTO
                .builder()
                .url(url.getOriginalUrl())
                .shortURL(url.getShortCode())
                .status(response.getStatus())
                .username(user.getUsername())
                .build();
    }

    public String getOriginalUrl(String shortURL) {
        String cachedUrl = redisTemplate.opsForValue().get(shortURL);
        if (cachedUrl != null) {
            return cachedUrl;
        }
        Url url = urlRepository.findByShortCode(shortURL)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
        redisTemplate.opsForValue().set(shortURL, url.getOriginalUrl(), 5, TimeUnit.HOURS);
        return url.getOriginalUrl();
    }


}
