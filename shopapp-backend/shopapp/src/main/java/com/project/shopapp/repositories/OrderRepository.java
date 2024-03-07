package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o WHERE (:userId IS NULL OR o.user.id = :userId) AND " +
            "(:keyword IS NULL OR :keyword = '' OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword% OR o.email LIKE %:keyword%)")
    Page<Order> findByUserId(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT o FROM Order o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword% OR o.email LIKE %:keyword%)")
    Page<Order> findByKeyword(String keyword, Pageable pageable);
    @Query("SELECT o FROM Order o WHERE (:userId = o.user.id)")
    List<Order> findOrdersByUserId(Long userId);
}
