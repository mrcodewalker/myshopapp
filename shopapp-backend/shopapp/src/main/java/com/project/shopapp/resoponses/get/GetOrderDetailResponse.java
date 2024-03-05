package com.project.shopapp.resoponses.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.OrderDetailResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDetailResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("order_detail")
    private List<OrderDetailResponse> orderDetailResponse;
}
