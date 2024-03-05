package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.resoponses.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(()->
                        new DateTimeException("Cannot find Category with id: "+productDTO.getCategoryId()));
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find this product with id: "+id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(
            String keyword,
            Long categoryId,
            PageRequest pageRequest) {
        Page<Product> productPage;
        productPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productPage.map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(
            long id,
            ProductDTO productDTO
    )
            throws Exception {
        Product existingProduct = this.getProductById(id);
        Product clone = this.getProductById(id);
        if (existingProduct!=null) {
            Category existingCategory = this.categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id = "+productDTO.getCategoryId()));
            if (productDTO.getThumbnail() != null) {
                existingProduct.setThumbnail(productDTO.getThumbnail());
            }

            if (productDTO.getPrice() != null) {
                existingProduct.setPrice(productDTO.getPrice());
            }

            if (existingCategory != null) {
                existingProduct.setCategory(existingCategory);
            }

            if (productDTO.getDescription() != null) {
                existingProduct.setDescription(productDTO.getDescription());
            }

            if (productDTO.getName() != null) {
                existingProduct.setName(productDTO.getName());
            }

            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> product = this.productRepository.findById(id);
        product.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
       return (productRepository.existsByName(name));
    }
    @Override
    @Transactional
    public ProductImage createProductImage (
            Long productId,
            ProductImageDTO productImageDTO
    ) throws Exception{
        Product existingProduct = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id = "+productImageDTO.getProductId()));
        ProductImage productImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size>= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Cannot create upper than "+ ProductImage.MAXIMUM_IMAGES_PER_PRODUCT +"files image");
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Override
    public Page<Product> getProductsByKeyword(String keyword, Pageable pageable) {
        return this.productRepository.findByKeyword(keyword, pageable);
    }
}
