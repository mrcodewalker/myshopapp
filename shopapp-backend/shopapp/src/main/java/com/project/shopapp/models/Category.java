package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.ValueGenerationType;
import org.springframework.stereotype.Component;

@Entity
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;
}
