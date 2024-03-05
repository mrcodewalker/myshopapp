package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.resoponses.OrderDetailResponse;
import com.project.shopapp.resoponses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Can not find order with id: "+orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id: "+orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .price(orderDetailDTO.getPrice())
                .color(orderDetailDTO.getColor())
                .build();
        orderDetailRepository.save(orderDetail);
        modelMapper.typeMap(OrderDetailDTO.class, OrderDetailResponse.class);
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.map(orderDetailDTO,orderDetailResponse);
        orderDetailResponse.setFullName(order.getUser().getFullName());
        return orderDetailResponse;
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find order detail with id: "+id));
        modelMapper.typeMap(OrderDetail.class,OrderDetailResponse.class);
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.map(orderDetail,orderDetailResponse);
        orderDetailResponse.setFullName(orderDetail.getOrder().getUser().getFullName());
        return orderDetailResponse;
    }

    @Override
    @Transactional
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws Exception {
        Order order = orderRepository.findById(newOrderDetailData.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Can not find order with id: "+newOrderDetailData.getOrderId()));
        Product product = productRepository.findById(newOrderDetailData.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id: "+newOrderDetailData.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .color(newOrderDetailData.getColor())
                .price(newOrderDetailData.getPrice())
                .order(order)
                .totalMoney(newOrderDetailData.getTotalMoney())
                .numberOfProducts(newOrderDetailData.getNumberOfProducts())
                .product(product)
                .id(newOrderDetailData.getOrderId())
                .build();
        orderDetailRepository.save(orderDetail);
        modelMapper.typeMap(OrderDetail.class,OrderDetailResponse.class);
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        modelMapper.map(orderDetail,orderDetailResponse);
        orderDetailResponse.setFullName(order.getUser().getFullName());
        return orderDetailResponse;
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long id) {
        this.orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails){
            modelMapper.typeMap(OrderDetail.class,OrderDetailResponse.class);
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            modelMapper.map(orderDetail,orderDetailResponse);
            orderDetailResponse.setFullName(orderDetail.getOrder().getUser().getFullName());
            orderDetailResponses.add(orderDetailResponse);
        }
        return orderDetailResponses;
    }
}
