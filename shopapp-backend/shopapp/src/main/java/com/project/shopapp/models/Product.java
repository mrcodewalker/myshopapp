package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "products")
@Builder
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false,length = 350)
    private String name;
    private Float price;
    @Column(name = "thumbnail",length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ProductImage> productImages;
}
