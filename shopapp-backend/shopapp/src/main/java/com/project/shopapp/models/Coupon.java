package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;
    @Column(name = "discount_value")
    private Float discountValue;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "expiration_date")
    private Date expirationDate;
}
