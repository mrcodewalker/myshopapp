package com.project.shopapp.resoponses.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteOrderResponse {
    @JsonProperty("message")
    private String message;
}
