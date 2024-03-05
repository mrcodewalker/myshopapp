package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {
    public final ProductImageRepository productImageRepository;
    public final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductImage createProductImage(ProductImageDTO productImageDTO) throws InvalidParamException {
        Product product = productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new InvalidParamException("Khong the tim thay id"));
        ProductImage productImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        productImageRepository.save(productImage);
        return productImage;
    }

    @Override
    public List<ProductImage> getProductImageById(Long id) {
        return this.productImageRepository.findByProductId(id);
    }

    @Override
    public List<ProductImage> getAllProductImages() {
        return this.productImageRepository.findAll();
    }

    @Override
    @Transactional
    public ProductImage updateProductImage(Long id, ProductImageDTO productImageDTO) {
        return null;
    }

    @Override
    @Transactional
    public void deleteProductImage(Long id) {

    }
}
