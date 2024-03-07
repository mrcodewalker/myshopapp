package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    Comment createComment(CommentDTO commentDTO) throws Exception;
    Comment getCommentById(long id) throws Exception;
    List<Comment> getAllComments() throws Exception;
    List<Comment> findByOrderId(Long orderId) throws Exception;
    List<Comment> findByProductId(Long productId) throws Exception;

    Comment updateComment(long id, CommentDTO commentDTO) throws Exception;
    void deleteComment(long id);
    Page<Comment> getOrdersByKeyword(String keyword, Pageable pageable);
}
