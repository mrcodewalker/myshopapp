package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.ProductImage;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadProductImagesResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("images_list")
    private List<ProductImage> imageList;
}
