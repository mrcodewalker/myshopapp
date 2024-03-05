package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1,message = "User's ID must be > 0")
    private Long productId;

    @Size(min = 5,max = 200,message = "Image's name")
    @JsonProperty("image_url")
    private String imageUrl;
}
