package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListUsersResponse {
    @JsonProperty("users")
    private List<UserResponse> users;
    @JsonProperty("total_pages")
    private int totalPages;
}
