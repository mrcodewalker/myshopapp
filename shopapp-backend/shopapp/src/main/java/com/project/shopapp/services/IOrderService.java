package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.UpdateOrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.resoponses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    Page<Order> getOrders(Long userId, String keyword, Pageable pageable) throws Exception;
    OrderResponse getOrder(Long id) throws Exception;
    OrderResponse updateOrder(Long id, UpdateOrderDTO updateOrderDTO) throws Exception;
    void deleteOrder(long id) throws Exception;
    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);
    List<Order> getOrdersByUserId(Long userId);
}
