package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class FacebookDTO {
    @JsonProperty("facebook_id")
    private String facebookId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
}
