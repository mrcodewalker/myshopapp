package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;
    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private String status;
    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;
}
