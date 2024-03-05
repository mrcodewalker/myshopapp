package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private String address;
    @JsonProperty("note")
    private String note;
    @JsonProperty("order_date")
    private Date orderDate;
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

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("order_details")
    private List<OrderDetail> orderDetails;

    @JsonProperty("active")
    private boolean active;

    public static OrderResponse fromOrder(Order order){
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .address(order.getAddress())
                .orderDetails(order.getOrderDetails())
                .fullName(order.getFullName())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getPaymentMethod())
                .phoneNumber(order.getPhoneNumber())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .shippingMethod(order.getShippingMethod())
                .status(order.getStatus())
                .email(order.getEmail())
                .totalMoney(order.getTotalMoney())
                .trackingNumber(order.getTrackingNumber())
                .userId(order.getUser().getId())
                .active(order.isActive())
                .build();
        return orderResponse;
    }
}
