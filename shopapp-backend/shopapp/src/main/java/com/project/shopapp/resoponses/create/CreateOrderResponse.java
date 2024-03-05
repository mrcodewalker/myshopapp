package com.project.shopapp.resoponses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.OrderResponse;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("order")
    private OrderResponse orderResponse;
}
