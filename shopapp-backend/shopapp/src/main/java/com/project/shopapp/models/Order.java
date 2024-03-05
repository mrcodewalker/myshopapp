package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.shopapp.dtos.OrderDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Builder
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname",nullable = false,length = 100)
    private String fullName;

    @Column(name = "phone_number",nullable = false,length = 20)
    private String phoneNumber;

    @Column(name = "email",length = 100)
    private String email;

    @Column(name = "address",length = 200)
    private String address;

    @Column(name = "note",length = 100)
    private String note;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "total_money",nullable = false)
    @Min(value = 0,message = "The value of package must be greater than 0 or equal 0")
    private Float totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
