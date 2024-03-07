package com.project.shopapp.services;

import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.UpdateOrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.resoponses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Can not find User with id = "+orderDTO.getUserId()));
        // ModelMapper
        // Convert OrderDTO.class to Order.class skip id information
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Data must be at least today !");
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate shippingLocalDate = currentDate.plusDays(7);
        order.setShippingDate(shippingLocalDate);
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setActive(false);

        Float money = 0.0F;

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            Long productId = cartItemDTO.getProductId();
            Long quantity = cartItemDTO.getQuantity();


            Product product = productRepository
                    .findById(productId)
                    .orElseThrow(() -> new DataNotFoundException(
                            "Can not find product with id: "+productId
                    ));

                Float price = product.getPrice();
                orderDetail.setProduct(product);
                orderDetail.setNumberOfProducts(quantity);
                orderDetail.setPrice(price);
                orderDetail.setTotalMoney(price*quantity);
                orderDetail.setColor("");
                money += price*quantity;

            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);
        modelMapper.typeMap(Order.class,OrderResponse.class);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public Page<Order> getOrders(Long userId, String keyword, Pageable pageable) throws Exception {
        Page<Order> orders = orderRepository.findByUserId(userId,keyword, pageable);
        return orders;
    }

    @Override
    public OrderResponse getOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Can not find order with id: "+id
                ));
        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setOrderDetails(order.getOrderDetails());
        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, UpdateOrderDTO updateOrderDTO) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find order with id: "+id));
        modelMapper.typeMap(UpdateOrderDTO.class,Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(updateOrderDTO,order);
        order.setUser(userRepository.findById(updateOrderDTO.getUserId())
                .orElseThrow(()->new DataNotFoundException("Can not find user with user id: "+updateOrderDTO.getUserId())));
        order.setOrderDate(new Date());
        order.setShippingDate(updateOrderDTO.getShippingDate());
        order.setStatus(updateOrderDTO.getStatus());
        order.setId(id);
        if (updateOrderDTO.getStatus().toUpperCase().equals("DELIVERED")&&
                updateOrderDTO.getPaymentMethod().toUpperCase().equals("COD")){
            order.setActive(true);
        }
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrder(long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Can not find order with id: "+id));
        order.setActive(false);
        this.orderRepository.save(order);
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
        return orderRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return this.orderRepository.findOrdersByUserId(userId);
    }
}
