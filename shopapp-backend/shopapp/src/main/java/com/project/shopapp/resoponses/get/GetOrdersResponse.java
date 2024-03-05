package com.project.shopapp.resoponses.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.OrderResponse;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrdersResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("orders")
    private List<OrderResponse> orderResponses;
}
