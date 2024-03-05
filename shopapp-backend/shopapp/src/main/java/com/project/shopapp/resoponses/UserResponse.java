package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse{
    @JsonProperty("id")
    private Long id;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;
    @JsonProperty("role")
    private Role role;
    public static UserResponse fromUser(User user){
        UserResponse userResponse = UserResponse.builder()
                .dateOfBirth(user.getDateOfBirth())
                .fullName(user.getFullName())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .address(user.getAddress())
                .id(user.getId())
                .active(user.isActive())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return userResponse;
    }
}
