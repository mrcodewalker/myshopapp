package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.User;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("user")
    private User user;
}
