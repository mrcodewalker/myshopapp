package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Comment;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.CommentRepository;
import com.project.shopapp.resoponses.CommentResponse;
import com.project.shopapp.resoponses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public Comment createComment(CommentDTO commentDTO) throws Exception {
        if (commentDTO.getRating()!=0&&commentDTO.getRating()>0){
            User user = userService.getUserById(commentDTO.getUserId());
            Product product = productService.getProductById(commentDTO.getProductId());
            OrderResponse orderResponse = orderService.getOrder(commentDTO.getOrderId());
            Order order = Order.builder()
                    .note(orderResponse.getNote())
                    .email(orderResponse.getEmail())
                    .orderDetails(orderResponse.getOrderDetails())
                    .orderDate(orderResponse.getOrderDate())
                    .phoneNumber(orderResponse.getPhoneNumber())
                    .fullName(orderResponse.getFullName())
                    .address(orderResponse.getAddress())
                    .shippingDate(orderResponse.getShippingDate())
                    .shippingMethod(orderResponse.getShippingMethod())
                    .totalMoney(orderResponse.getTotalMoney())
                    .paymentMethod(orderResponse.getPaymentMethod())
                    .shippingAddress(orderResponse.getShippingAddress())
                    .trackingNumber(orderResponse.getTrackingNumber())
                    .status(orderResponse.getStatus())
                    .id(orderResponse.getId())
                    .user(user)
                    .build();
            Comment comment = Comment.builder()
                    .commentContent(commentDTO.getCommentContent())
                    .product(product)
                    .user(user)
                    .active(true)
                    .order(order)
                    .rating(commentDTO.getRating())
                    .build();
            return this.commentRepository.save(comment);
        } else {
            throw new DataNotFoundException("Can not create comment, please try again!");
        }
    }

    @Override
    public Comment getCommentById(long id) throws Exception{
        return this.commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find message with id: "+id));
    }

    @Override
    public List<Comment> getAllComments() {
        return this.commentRepository.findAll();
    }

    @Override
    public List<Comment> findByOrderId(Long orderId) throws Exception {
        return this.commentRepository.findByOrderId(orderId);
    }

    @Override
    public List<Comment> findByProductId(Long productId) throws Exception {
        return this.commentRepository.findByProductId(productId);
    }

    @Override
    public Comment updateComment(long id, CommentDTO commentDTO) throws Exception {
        Comment comment = this.getCommentById(id);
        User user = userService.getUserById(commentDTO.getUserId());
        Product product = productService.getProductById(commentDTO.getProductId());
        if (commentDTO.getRating()<0){
            throw new DataNotFoundException("Can not update comment with rating score lower than 0");
        }
        if (commentDTO.getCommentContent()==""||commentDTO == null){
            throw new DataNotFoundException("Can not update comment with no new value");
        }
        return Comment.builder()
                .commentContent(commentDTO.getCommentContent())
                .rating(commentDTO.getRating())
                .user(user)
                .id(id)
                .build();
    }

    @Override
    public void deleteComment(long id) {
        this.commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> getOrdersByKeyword(String keyword, Pageable pageable) {
        return null;
    }
}
