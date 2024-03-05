package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangesPasswordResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("fullname")
    private String fullname;
}
