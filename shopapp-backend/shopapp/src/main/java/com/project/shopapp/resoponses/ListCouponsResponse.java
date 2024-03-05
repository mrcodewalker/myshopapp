package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Coupon;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCouponsResponse {
    @JsonProperty("coupons")
    private List<CouponResponse> coupons;
    @JsonProperty("total_pages")
    private int totalPages;
}
