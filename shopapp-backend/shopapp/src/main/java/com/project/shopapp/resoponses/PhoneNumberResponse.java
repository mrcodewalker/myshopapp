package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberResponse {
    @JsonProperty("phone_number")
    public String phoneNumber;
    @JsonProperty("avatar")
    public String avatar;
}
