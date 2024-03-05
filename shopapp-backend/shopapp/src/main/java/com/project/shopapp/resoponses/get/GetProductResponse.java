package com.project.shopapp.resoponses.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.ProductResponse;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("product")
    private ProductResponse productResponse;
}
