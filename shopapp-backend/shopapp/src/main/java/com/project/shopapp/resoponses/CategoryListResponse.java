package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryListResponse {
    @JsonProperty("categories")
    private List<CategoryResponse> categoryResponses;
    @JsonProperty("total_pages")
    private int totalPages;
}
