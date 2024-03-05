package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductResponse {
    @JsonProperty("product")
    private Product product;
    @JsonProperty("message")
    private String message;
}
