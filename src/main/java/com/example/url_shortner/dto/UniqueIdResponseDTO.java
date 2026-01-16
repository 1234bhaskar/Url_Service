package com.example.url_shortner.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueIdResponseDTO {
    private int randomID;
    private String status;
}