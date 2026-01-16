package com.example.url_shortner.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortURLResponseDTO {

    private String url;
    private String shortURL;
    private String status;
}
