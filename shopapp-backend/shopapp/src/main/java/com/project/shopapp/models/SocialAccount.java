package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "social_accounts")
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider",nullable = false,length = 20)
    private String provider;
    @Column(name = "provider_id",nullable = false,length = 50)
    private String providerId;

    @Column(name = "email",length = 150)
    private String email;

    @Column(name="name",length = 100)
    private String name;

//    @ManyToOne
//    @Column(name = "user_id")
//    private User user;
}
