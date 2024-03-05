package com.project.shopapp.repositories;

import com.project.shopapp.models.Coupon;
import com.project.shopapp.resoponses.ListCouponsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    List<Coupon> findByCode(String code);
    @Query("SELECT c FROM Coupon c WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR c.code LIKE CONCAT('%', :keyword, '%'))")
    Page<Coupon> findByKeyword(String keyword, Pageable pageable);
}
