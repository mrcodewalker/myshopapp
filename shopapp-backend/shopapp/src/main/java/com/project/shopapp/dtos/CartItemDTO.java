package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class CartItemDTO {
    @JsonProperty("product_id")
    @Min(value = 1,message = "Order's ID must be greater than 1")
    private Long productId;

    @JsonProperty("quantity")
    @Min(value = 1, message = "Quantity must be greater than 1")
    private Long quantity;
}
