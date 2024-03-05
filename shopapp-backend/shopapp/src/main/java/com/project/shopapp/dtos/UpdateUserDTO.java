package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Setter
@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("password")
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;



}