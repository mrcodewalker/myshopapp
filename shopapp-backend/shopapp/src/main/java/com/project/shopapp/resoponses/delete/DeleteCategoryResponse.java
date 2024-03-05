package com.project.shopapp.resoponses.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCategoryResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
}