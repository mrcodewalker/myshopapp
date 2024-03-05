package com.project.shopapp.resoponses.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.resoponses.UserResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetUsersResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("users")
    private List<UserResponse> userResponseList;
}
