package com.project.shopapp.resoponses.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.OrderDetailResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderDetailResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("order_detail")
    private OrderDetailResponse orderDetailResponse;
}
