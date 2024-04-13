package com.project.shopapp.repositories;

import com.project.shopapp.models.Email;
import com.project.shopapp.models.Facebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacebookRepository extends JpaRepository<Facebook, Long> {
    List<Facebook> findByEmail(String email);
    Facebook findUserByEmail(String email);
    Facebook findByFacebookId(String facebookId);
}
