package com.project.shopapp.resoponses.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeleteProductResponse {
    @JsonProperty("message")
    private String message;
}
