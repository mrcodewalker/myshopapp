package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.resoponses.OrderDetailResponse;
import com.project.shopapp.resoponses.create.CreateOrderDetailResponse;
import com.project.shopapp.resoponses.delete.DeleteOrderDetailResponse;
import com.project.shopapp.resoponses.get.GetOrderDetailResponse;
import com.project.shopapp.resoponses.update.UpdateOrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodeFormatter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final LocalizationUtils localizationUtils;
    private final IOrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
        @Valid @RequestBody OrderDetailDTO orderDetailDTO,
        BindingResult result,
        HttpServletRequest request
    ){
        try{
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
            }
            OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(CreateOrderDetailResponse
                    .builder()
                            .orderDetailResponse(orderDetailResponse)
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(MessageKeys.CREATE_ORDER_DETAIL))
                    .build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
       @Valid @PathVariable("id") Long id
    ) throws Exception {
        OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetailResponse);
    }
    // Lay ra danh sach cac order_details cua 1 order nao do
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId,
            HttpServletRequest request
    ){
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.findByOrderId(orderId);
        return ResponseEntity.ok(
                GetOrderDetailResponse.builder()
                        .message(
                                localizationUtils
                                        .getLocalizedMessage(
                                                MessageKeys.GET_ORDER_DETAIL
                                        )
                        )
                        .orderDetailResponse(orderDetailResponses)
                        .build()
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            HttpServletRequest request,
            @RequestBody OrderDetailDTO newOrderDetailData
            ) throws Exception {
        OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id,newOrderDetailData);
        return ResponseEntity.ok(UpdateOrderDetailResponse.builder()
                        .message(
                                localizationUtils
                                        .getLocalizedMessage(
                                                MessageKeys.UPDATE_ORDER_DETAIL
                                        )
                        )
                        .orderDetailResponse(orderDetailResponse)
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
        @Valid @PathVariable("id") Long id,
        HttpServletRequest request
    ){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(
                DeleteOrderDetailResponse.builder()
                        .message(
                                localizationUtils
                                        .getLocalizedMessage(
                                                MessageKeys.DELETE_ORDER_DETAIL, id
                                        )
                        )
                        .build()
        );
    }
}
