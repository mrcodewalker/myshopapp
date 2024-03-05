package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.CouponDTO;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.resoponses.CouponResponse;
import com.project.shopapp.resoponses.ListCouponsResponse;
import com.project.shopapp.resoponses.OrderListResponse;
import com.project.shopapp.resoponses.OrderResponse;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.services.ICouponService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/coupons")
public class CouponController {
    private final ICouponService couponService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> createCoupon(
            @Valid @RequestBody CouponDTO couponDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            couponDTO.setExpirationDate(calendar.getTime());
            return ResponseEntity.ok(couponService.createCoupon(couponDTO));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    localizationUtils.getLocalizedMessage(MessageKeys.CAN_NOT_CREATE_COUPON)
            );
        }
    }
    @GetMapping("/{id}")
    private ResponseEntity<?> getCouponById(
            @PathVariable("id") Long id
    ){
        try{
            return ResponseEntity.ok(couponService.getCouponById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/list")
    private ResponseEntity<?> getAllCoupons(){
        try{
            return ResponseEntity.ok(couponService.getAllCoupons());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    localizationUtils.getLocalizedMessage(
                            MessageKeys.GET_COUPONS_FAILED
                    )
            );
        }
    }
    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCouponsByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending()
        );
        Page<CouponResponse> couponsPage = this.couponService
                .findByKeyword(keyword, pageRequest)
                .map(CouponResponse::fromCoupon);
        int totalPages = couponsPage.getTotalPages();
        List<CouponResponse> couponResponses = couponsPage.getContent();
        return ResponseEntity.ok(ListCouponsResponse
                .builder()
                .coupons(couponResponses)
                .totalPages(totalPages)
                .build());
    }
    @PutMapping("/{id}")
    private ResponseEntity<?> updateById(
            @PathVariable("id") Long id,
            @Valid @RequestBody CouponDTO couponDTO
    ){
        try{
            return ResponseEntity.ok(this.couponService.updateCoupon(id, couponDTO));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
              e.getMessage()
            );
        }
    }
    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteById(
            @PathVariable("id") Long id
    ) {
        try {
            this.couponService.deleteCoupon(id);
            return ResponseEntity.ok("Delete coupon with id: " + id + " successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
