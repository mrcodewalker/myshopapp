package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.resoponses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO);
    Product getProductById(long id) throws Exception;
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    public ProductImage createProductImage (Long productId, ProductImageDTO productImageDTO) throws Exception;
    List<Product> findProductsByIds(List<Long> productIds);
    Page<Product> getProductsByKeyword(String keyword, Pageable pageable);
}
