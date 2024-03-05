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
public class CouponDTO {
    @NotBlank
    @JsonProperty("code")
    private String code;

    @JsonProperty("discount_value")
    private Float discountValue;

    @JsonProperty("expiration_date")
    private Date expirationDate;

    @JsonProperty("discount_type")
    private String discountType;
}
