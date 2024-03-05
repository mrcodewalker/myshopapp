package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.ProductImage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("image_url")
    private String imageUrl;
}
