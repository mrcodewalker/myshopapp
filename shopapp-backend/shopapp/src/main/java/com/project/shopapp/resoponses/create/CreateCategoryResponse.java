package com.project.shopapp.resoponses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
}
