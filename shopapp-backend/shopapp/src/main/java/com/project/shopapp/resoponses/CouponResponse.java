package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Coupon;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("discount_value")
    private Float discountValue;

    @JsonProperty("discount_type")
    private String discountType;

    @JsonProperty("expiration_date")
    private Date expirationDate;
    public static CouponResponse fromCoupon(
            Coupon coupon
    ){
        return CouponResponse.builder()
                .code(coupon.getCode())
                .id(coupon.getId())
                .discountType(coupon.getDiscountType())
                .expirationDate(coupon.getExpirationDate())
                .discountValue(coupon.getDiscountValue())
                .build();
    }
}
