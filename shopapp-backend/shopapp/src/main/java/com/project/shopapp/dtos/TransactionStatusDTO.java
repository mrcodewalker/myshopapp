package com.project.shopapp.dtos;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatusDTO {
    private String status;
    private String message;
    private String data;
}
