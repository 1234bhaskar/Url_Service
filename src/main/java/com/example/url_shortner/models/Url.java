package com.example.url_shortner.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls", indexes = @Index(name = "idx_short_code", columnList = "short_code"))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT",name = "original_url")
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 10,name = "short_code")
    private String shortCode;

    private LocalDateTime createdAt;

    @Column(name = "click_count")
    private Long clickCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}


