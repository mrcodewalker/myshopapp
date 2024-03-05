package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",nullable = false,length = 255)
    private String token;

    @Column(name = "token_type",nullable = false,length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
