package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListResponse {
    @JsonProperty("orders")
    private List<OrderResponse> orders;
    @JsonProperty("total_pages")
    private int totalPages;

}