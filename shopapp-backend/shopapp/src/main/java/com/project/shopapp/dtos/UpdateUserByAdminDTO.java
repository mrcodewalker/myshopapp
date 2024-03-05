package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserByAdminDTO {
    @JsonProperty("role_name")
    private String roleName;
    @JsonProperty("is_active")
    private boolean isActive;
}
