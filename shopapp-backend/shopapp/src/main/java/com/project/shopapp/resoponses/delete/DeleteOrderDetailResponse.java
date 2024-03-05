package com.project.shopapp.resoponses.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteOrderDetailResponse {
    @JsonProperty("message")
    private String message;
}
