package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.ProductImage;

import java.util.List;

public interface IProductImageService {
    ProductImage createProductImage(ProductImageDTO productImageDTO) throws InvalidParamException;
    List<ProductImage> getProductImageById(Long id);
    List<ProductImage> getAllProductImages();
    ProductImage updateProductImage(Long id, ProductImageDTO productImageDTO);
    void deleteProductImage(Long id);
}
