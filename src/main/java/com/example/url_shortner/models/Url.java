package com.example.url_shortner.models;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "urls", indexes = @Index(name = "idx_short_code", columnList = "shortCode"))
@Getter
@Setter
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 10)
    private String shortCode;

    private LocalDateTime createdAt;

    private Long clickCount = 0L;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}


