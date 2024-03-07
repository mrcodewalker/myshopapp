package com.project.shopapp.repositories;

import com.project.shopapp.models.Category;
import com.project.shopapp.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR c.commentContent LIKE %:keyword% )")
    Page<Comment> findByKeyword(String keyword, Pageable pageable);
    List<Comment> findByOrderId(Long orderId);
    List<Comment> findByProductId(Long productId);

}
