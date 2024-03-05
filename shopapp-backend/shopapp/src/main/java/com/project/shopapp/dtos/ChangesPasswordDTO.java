package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ChangesPasswordDTO {
    @NotBlank
    @JsonProperty("old_password")
    private String oldPassword;

    @JsonProperty("password")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

}
