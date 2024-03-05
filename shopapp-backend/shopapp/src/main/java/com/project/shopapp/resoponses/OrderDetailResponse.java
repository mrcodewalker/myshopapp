package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.repositories.OrderDetailRepository;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("price")
    private Float price;
    @JsonProperty("number_of_products")
    private Long numberOfProducts;
    @JsonProperty("total_money")
    private Float totalMoney;
    private String color;
    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .color(orderDetail.getColor())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .fullName(orderDetail.getOrder().getUser().getFullName())
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
