package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.CouponDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Coupon;
import com.project.shopapp.resoponses.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICouponService {
    CouponResponse createCoupon(CouponDTO couponDTO) throws InvalidParamException;
    CouponResponse getCouponById(long id) throws DataNotFoundException;
    List<CouponResponse> getAllCoupons();
    CouponResponse updateCoupon(long id, CouponDTO couponDTODTO) throws DataNotFoundException, InvalidParamException;
    void deleteCoupon(long id);
    Page<Coupon> findByKeyword(String keyword, Pageable pageable);

}
