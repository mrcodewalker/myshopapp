package com.project.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.type.descriptor.jdbc.TinyIntJdbcType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Setter
@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @NotBlank(message = "Password cannot be blank")
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;
}
