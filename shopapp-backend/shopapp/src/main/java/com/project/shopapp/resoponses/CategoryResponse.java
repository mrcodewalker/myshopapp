package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Category;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    public static CategoryResponse fromCategory(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
