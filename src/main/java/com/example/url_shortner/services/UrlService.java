package com.example.url_shortner.services;

import com.example.url_shortner.dto.ShortURLResponseDTO;
import com.example.url_shortner.dto.UniqueIdResponseDTO;
import com.example.url_shortner.models.Url;
import com.example.url_shortner.repositories.UrlRepository;
import com.example.url_shortner.utils.UrlUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UrlService {
    private final WebClient webClient;
    private final UrlUtil urlUtil;
    private final UrlRepository urlRepository;

    public UrlService(WebClient webClient, UrlUtil urlUtil, UrlRepository urlRepository) {
    this.webClient=webClient;
    this.urlUtil=urlUtil;
    this.urlRepository = urlRepository;
    }

    public ShortURLResponseDTO generateShortURLService(String originalUrl) {
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
        Url urlObj=Url.builder().originalUrl(originalUrl).shortCode(code).clickCount(0L).build();
        Url url= urlRepository.save(urlObj);
        return ShortURLResponseDTO.builder().url(url.getOriginalUrl()).shortURL(url.getShortCode()).status(response.getStatus()).build();
    }

}
