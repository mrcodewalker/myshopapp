package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.controllers.ProductController;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.services.ProductImageService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends BaseResponse {
    @JsonProperty("id")
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();
    public static ProductResponse fromProduct(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .price(product.getPrice())
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .productImages(product.getProductImages())
                .build();
        productResponse.setUpdatedAt(product.getUpdatedAt());
        productResponse.setCreatedAt(product.getCreatedAt());
        return productResponse;
    }
}
