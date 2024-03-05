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
public class DistrictDTO {
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("district_id")
    private Long districtId;
}
