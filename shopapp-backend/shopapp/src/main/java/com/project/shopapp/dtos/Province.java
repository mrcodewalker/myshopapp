package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Province {
    @JsonProperty("province_name")
    private String provinceName;
    @JsonProperty("province_id")
    private Long provinceId;
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("district_id")
    private Long districtId;
    @JsonProperty("commune_name")
    private String communeName;
    @JsonProperty("commune_id")
    private Long communeId;
}
