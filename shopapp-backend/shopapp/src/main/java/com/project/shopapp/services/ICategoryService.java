package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long id, CategoryDTO categoryDTO);
    void deleteCategory(long id);
    Page<Category> getOrdersByKeyword(String keyword, Pageable pageable);
}
