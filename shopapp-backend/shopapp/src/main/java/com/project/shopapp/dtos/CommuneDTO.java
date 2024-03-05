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
public class CommuneDTO {
    @JsonProperty("commune_name")
    private String communeName;
    @JsonProperty("commune_id")
    private Long communeId;
}
