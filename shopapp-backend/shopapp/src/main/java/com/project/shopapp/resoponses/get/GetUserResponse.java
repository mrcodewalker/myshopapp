package com.project.shopapp.resoponses.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetUserResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private UserResponse userResponse;
}
