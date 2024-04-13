package com.project.shopapp.repositories;

import com.project.shopapp.models.Coupon;
import com.project.shopapp.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findByEmail(String email);
    Email findUserByEmail(String email);
}
