package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.BaseEntity;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {
    @JsonProperty("products")
    private List<ProductResponse> products;
    @JsonProperty("total_pages")
    private int totalPages;

}
