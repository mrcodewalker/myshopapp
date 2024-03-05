package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO implements Serializable {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;
    @JsonProperty("url")
    private String url;
}
