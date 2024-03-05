package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Password is required")
    private String password;
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("role_id")
    private Long roleId;
}
