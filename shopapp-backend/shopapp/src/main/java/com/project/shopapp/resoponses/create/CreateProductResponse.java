package com.project.shopapp.resoponses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.ProductResponse;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("product")
    private ProductResponse productResponse;
}
