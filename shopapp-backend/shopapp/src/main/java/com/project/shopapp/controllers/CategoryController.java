package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.resoponses.CategoryListResponse;
import com.project.shopapp.resoponses.CategoryResponse;
import com.project.shopapp.resoponses.OrderListResponse;
import com.project.shopapp.resoponses.OrderResponse;
import com.project.shopapp.resoponses.create.CreateCategoryResponse;
import com.project.shopapp.resoponses.delete.DeleteCategoryResponse;
import com.project.shopapp.resoponses.update.UpdateCategoryResponse;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
//@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    // Show all of categories
    @PostMapping("")
    // Neu tham so truyen vao la 1 object thi sao? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    MessageKeys.CREATE_CATEGORY_FAILED
            );
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(CreateCategoryResponse.builder()
                        .message(localizationUtils
                                .getLocalizedMessage(
                                        MessageKeys.CREATE_CATEGORY_SUCCESSFULLY
                                ))
                .build());
    }
    @GetMapping("") // http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page")    int page,
            @RequestParam("limit")   int limit
    ){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long id
            , @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,
                categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(
                                MessageKeys.UPDATE_CATEGORY
                        ))
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteCategoryResponse> deleteCategory(@PathVariable Long id,
                                                 HttpServletRequest request){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(DeleteCategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(
                                MessageKeys.DELETE_CATEGORY, id
                        ))
                .build());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCategoryById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(this.categoryService.getCategoryById(id));
    }
    @GetMapping("/get-categories-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryListResponse> getCategoriesByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<CategoryResponse> categoryPage = this.categoryService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(CategoryResponse::fromCategory);
        int totalPages = categoryPage.getTotalPages();
        List<CategoryResponse> categoryResponses = categoryPage.getContent();
        return ResponseEntity.ok(CategoryListResponse
                .builder()
                .categoryResponses(categoryResponses)
                .totalPages(totalPages)
                .build());
    }
}
