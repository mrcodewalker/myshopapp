package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1,message = "User's ID must be greater than 0")
    private Long userId;
    @JsonProperty("fullname")
    @NotBlank(message = "You must type down your name")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Please enter your phone number")
    @Size(min = 5,message = "Phone number size must be greater than 5")
    private String phoneNumber;

    @NotBlank(message = "Please enter your address to receive package")
    private String address;

    private String note;
    @JsonProperty("total_money")
    @Min(value = 0,message = "Total money must be greater than 0")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    @NotBlank(message = "Please enter your shipping method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    @NotBlank(message = "Please enter your shipping address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("payment_method")
    @NotBlank(message = "Please choose one payment method")
    private String paymentMethod;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;
}
