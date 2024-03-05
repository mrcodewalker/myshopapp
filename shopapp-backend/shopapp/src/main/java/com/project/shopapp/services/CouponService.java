package com.project.shopapp.services;

import com.project.shopapp.dtos.CouponDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Coupon;
import com.project.shopapp.repositories.CouponRepository;
import com.project.shopapp.resoponses.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {
    private final CouponRepository couponRepository;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public CouponResponse createCoupon(CouponDTO couponDTO) throws InvalidParamException {
        List<Coupon> coupons = this.couponRepository
                .findByCode(couponDTO.getCode());

        if (!coupons.isEmpty()){
            throw new InvalidParamException("Can not create new coupon with code: "+couponDTO.getCode());
        }

        Coupon coupon = Coupon.builder()
                .discountValue(couponDTO.getDiscountValue())
                .code(couponDTO.getCode())
                .discountType(couponDTO.getDiscountType())
                .expirationDate(couponDTO.getExpirationDate())
                .build();

        CouponResponse couponResponse = CouponResponse.builder()
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .expirationDate(coupon.getExpirationDate())
                .build();
        couponRepository.save(coupon);
        return couponResponse;
    }

    @Override
    public CouponResponse getCouponById(long id) throws DataNotFoundException {
        Coupon coupon = this.couponRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find coupon with id: "+id
                ));
        return CouponResponse.builder()
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .expirationDate(coupon.getExpirationDate())
                .build();
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        List<Coupon> clone = this.couponRepository.findAll();
        List<CouponResponse> result = new ArrayList<>();
        for (Coupon coupon : clone){
            if (!coupon.getExpirationDate().before(new Date())){
                CouponResponse couponResponse = CouponResponse.builder()
                        .code(coupon.getCode())
                        .discountType(coupon.getDiscountType())
                        .discountValue(coupon.getDiscountValue())
                        .expirationDate(coupon.getExpirationDate())
                        .build();
                result.add(couponResponse);
            }
        }
        return result;
    }

    @Override
    public CouponResponse updateCoupon(long id, CouponDTO couponDTO) throws DataNotFoundException, InvalidParamException {
        List<Coupon> couponList = this.couponRepository.findByCode(couponDTO.getCode());

        if (!couponList.isEmpty()){
            throw new InvalidParamException("Can not create new coupon with code: "+couponDTO.getCode());
        }

        Coupon coupon = Coupon.builder()
                .expirationDate(couponDTO.getExpirationDate())
                .code(couponDTO.getCode())
                .id(id)
                .discountValue(couponDTO.getDiscountValue())
                .discountType(couponDTO.getDiscountType())
                .build();
        CouponResponse couponResponse = CouponResponse.builder()
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .expirationDate(coupon.getExpirationDate())
                .build();
        this.couponRepository.save(coupon);
        return couponResponse;
    }

    @Override
    public void deleteCoupon(long id) {
        this.couponRepository.deleteById(id);
    }

    @Override
    public Page<Coupon> findByKeyword(String keyword, Pageable pageable) {
        return this.couponRepository.findByKeyword(keyword,pageable);
    }
}
