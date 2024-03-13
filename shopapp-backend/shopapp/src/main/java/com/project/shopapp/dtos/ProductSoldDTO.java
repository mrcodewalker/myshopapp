package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSoldDTO {
    @JsonProperty("message")
    private String message;
    @JsonProperty("quantity")
    private Long quantity;
}
