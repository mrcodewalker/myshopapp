package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.UpdateOrderDTO;
import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.User;
import com.project.shopapp.resoponses.*;
import com.project.shopapp.resoponses.create.CreateOrderResponse;
import com.project.shopapp.resoponses.delete.DeleteOrderResponse;
import com.project.shopapp.resoponses.get.GetOrdersResponse;
import com.project.shopapp.resoponses.update.UpdateOrderResponse;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result,
            HttpServletRequest request
    ){
        try{
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
            }
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(CreateOrderResponse.builder()
                            .orderResponse(orderResponse)
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(
                                                    MessageKeys.CREATE_ORDER
                                            )
                            )
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @PathVariable("id") Long id
    ){
        try{
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody UpdateOrderDTO updateOrderDTO,
            HttpServletRequest request
    ){
        try{
            OrderResponse orderResponse = orderService.updateOrder(id, updateOrderDTO);
            return ResponseEntity.ok(UpdateOrderResponse.builder()
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(
                                                    MessageKeys.UPDATE_ORDER
                                            )
                            )
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    // Xoa mem
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable("id") Long id,
            HttpServletRequest request
    ){
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.ok(DeleteOrderResponse.builder()
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(
                                                    MessageKeys.DELETE_ORDER, id
                                            )
                            )
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListResponse> getOrdersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderPage = orderService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(OrderResponse::fromOrder);
        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orderResponses = orderPage.getContent();
        return ResponseEntity.ok(OrderListResponse
                .builder()
                .orders(orderResponses)
                .totalPages(totalPages)
                .build());
    }
    @GetMapping("/list/{user_id}")
    public ResponseEntity<?> getOrders(
            @Valid @PathVariable("user_id") Long userId,
            HttpServletRequest request,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        try{
            PageRequest pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("id").descending()
            );
            Page<OrderResponse> orderPage = orderService
                    .getOrders(userId, keyword, pageRequest)
                    .map(OrderResponse::fromOrder);
            int totalPages = orderPage.getTotalPages();
            List<OrderResponse> orderResponses = orderPage.getContent();
            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            if (user.getId() != userId){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(OrderListResponse
                    .builder()
                    .orders(orderResponses)
                    .totalPages(totalPages)
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/get_orders_by_user_id/{id}")
    public ResponseEntity<?> getOrdersByUserId(
            @PathVariable("id") Long id
    ){
        try{
            List<OrderResponse> orderResponses = new ArrayList<>();
            for (Order order: this.orderService.getOrdersByUserId(id)){
                orderResponses.add(OrderResponse.fromOrder(order));
            }
            return ResponseEntity.ok(OrderListResponse
                    .builder()
                            .orders(orderResponses)
                    .build());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
