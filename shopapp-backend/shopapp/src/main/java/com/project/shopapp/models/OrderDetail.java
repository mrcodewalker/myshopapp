package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.aspectj.weaver.ast.Or;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price",nullable = false)
    @Min(value = 0,message = "Price must be greater than 0 or equal 0")
    private Float price;

    @Column(name = "number_of_products",nullable = false)
    @JsonProperty("quantity")
    @Min(value = 1,message = "Quantity must be greater than 0")
    private Long numberOfProducts;

    @Column(name = "total_money",nullable = false)
    @JsonProperty("total_money")
    @Min(value = 0,message = "total money must be greater than 0 or equal 0")
    private Float totalMoney;

    @Column(name = "color",length = 20)
    private String color;
}
