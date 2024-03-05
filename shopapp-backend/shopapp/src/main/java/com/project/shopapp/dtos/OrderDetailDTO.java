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
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "Order's ID must be greater than 1")
    private Long orderId;
    @JsonProperty("product_id")
    @Min(value = 1,message = "Product's ID must be greater than 1")
    private Long productId;
    @Min(value = 0,message = "Price must be greater than 0")
    private Float price;
    @Min(value = 1,message = "Number of products must be greater than 0")
    @JsonProperty("number_of_products")
    private Long numberOfProducts;
    @Min(value = 0,message = "Total money must be greater than 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

}
