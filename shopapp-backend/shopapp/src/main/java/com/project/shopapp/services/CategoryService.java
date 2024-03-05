package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Order;
import com.project.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
//    public CategoryService(CategoryRepository categoryRepository){
//        this.categoryRepository = categoryRepository;
//    }
    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName()).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(long id
            , CategoryDTO categoryDTO) {
        Category exsitingCategory = this.getCategoryById(id);
        exsitingCategory.setName(categoryDTO.getName());
        categoryRepository.save(exsitingCategory);
        return exsitingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        // Xóa cứng
        categoryRepository.deleteById(id);
    }
    @Override
    public Page<Category> getOrdersByKeyword(String keyword, Pageable pageable) {
        return this.categoryRepository.findByKeyword(keyword, pageable);
    }
}
