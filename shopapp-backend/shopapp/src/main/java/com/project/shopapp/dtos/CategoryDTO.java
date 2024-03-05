package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.management.ConstructorParameters;

@Data
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;
}
