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
public class ProvinceDTO {
    @JsonProperty("province_name")
    private String provinceName;
    @JsonProperty("province_id")
    private Long provinceId;
}
