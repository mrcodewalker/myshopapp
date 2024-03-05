package com.project.shopapp.repositories;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM Category c WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR c.name LIKE %:keyword% )")
    Page<Category> findByKeyword(String keyword, Pageable pageable);
}
