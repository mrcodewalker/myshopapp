package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.resoponses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetailResponse getOrderDetail(Long id) throws Exception;
    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws Exception;
    void deleteOrderDetail(Long id);
    List<OrderDetailResponse> findByOrderId(Long orderId);
}
