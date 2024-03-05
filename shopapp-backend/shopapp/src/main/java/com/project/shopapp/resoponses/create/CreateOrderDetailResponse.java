package com.project.shopapp.resoponses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.OrderDetailResponse;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDetailResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("order_detail")
    private OrderDetailResponse orderDetailResponse;
}
