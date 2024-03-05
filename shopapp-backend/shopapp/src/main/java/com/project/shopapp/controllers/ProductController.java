package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.resoponses.*;
import com.project.shopapp.resoponses.create.CreateProductResponse;
import com.project.shopapp.resoponses.delete.DeleteProductResponse;
import com.project.shopapp.services.IProductImageService;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final LocalizationUtils localizationUtils;
    private final IProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductImageService productImageService;
    private final ProductRepository productRepository;
@PostMapping(value = "")
public ResponseEntity<?> createProduct(
    @RequestParam(name = "name") String name,
    @RequestParam(name = "price") Float price,
    @RequestParam(name = "description") String description,
    @RequestParam(name = "category_id") Long categoryId,
    HttpServletRequest request
) throws Exception{
    try {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setPrice(price);
        productDTO.setDescription(description);
        productDTO.setCategoryId(categoryId);
        Product product = productService.createProduct(productDTO);
        return ResponseEntity.ok(
                CreateProductResponse.builder()
                        .message(
                                localizationUtils
                                        .getLocalizedMessage(
                                                MessageKeys.CREATE_PRODUCT
                                        )
                        )
                        .build()
        );
    } catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
@PostMapping(value = "/upload/{id}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> uploadImages(
    @RequestPart(name = "files") List<MultipartFile> files,
    @PathVariable(name = "id") Long id,
    HttpServletRequest request
) throws Exception{
    try {
        if (files.size()>ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            return ResponseEntity.badRequest().body(
                    MessageKeys.ERROR_MAX_5_IMAGES
            );
        }
        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    MessageKeys.FILE_MUST_BE_IMAGE
            );
        }
        List<ProductImage> listProductImage = new ArrayList<>();
        files = (files == null) ? new ArrayList<MultipartFile>() : files;
        for (MultipartFile file : files) {
            Product existingProduct = this.productService.getProductById(id);
            if (file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(
                                MessageKeys.FILE_LARGE
                        );
            }
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("You can't upload the file");
            }
            String fileName = storeFile(file);
            ProductImage productImage = ProductImage.builder()
                    .imageUrl(fileName)
                    .build();
            listProductImage.add(productImage);
            ProductImageDTO productImageDTO = ProductImageDTO.builder()
                    .productId(id)
                    .imageUrl(fileName)
                    .build();
            productService.createProductImage(id, productImageDTO);
        }
        return ResponseEntity.ok().body(
                UploadProductImagesResponse.builder()
                        .message(localizationUtils
                                .getLocalizedMessage(
                                        MessageKeys.UPLOAD_IMAGE_SUCCESSFULLY
                                ))
                        .imageList(listProductImage)
                        .build()
        );
    } catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
@GetMapping("/images/{imageName}")
public ResponseEntity<?> viewImage(
        @PathVariable String imageName
) {
    try{
        Path imagePath = Paths.get("uploads/"+imageName);
        UrlResource resource = new UrlResource(imagePath.toUri());

        if (resource.exists()){
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
        }
    } catch (Exception e){
        return ResponseEntity.notFound().build();
    }
}

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file)||file.getOriginalFilename()==null) {
            throw new IOException("Invalid image file format");
        }
        // Generate a unique filename using UUID
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // Save the file to the "uploads" directory
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = uploadDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), destination);

        // You can save the unique filename to the database or use it as needed
        // For example, you can set it in the ProductDTO's thumbnail field
        String thumbnail = "/api/products/uploads/" + uniqueFileName;
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    )
    {
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(keyword,categoryId,pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId){
        try {
            Product existingProduct = productService.getProductById(productId);
            ProductResponse productResponse = ProductResponse.fromProduct(existingProduct);
            productResponse.setId(productId);
            return ResponseEntity.ok(
                        productResponse
                    );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteProductResponse> deleteProduct(@PathVariable Long id){
        try {
            productService.deleteProduct(id);
            productImageService.deleteProductImage(id);
            return ResponseEntity.ok(
                    DeleteProductResponse.builder()
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(
                                                    MessageKeys.DELETE_PRODUCT, id
                                            )
                            )
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    DeleteProductResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }
//    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for (int i=0;i<1_000_000;i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(4, 8))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
            return ResponseEntity.ok("Fake Products created successfully");
    }
    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids){
        try{
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get-products-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductListResponse> getProductsByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<ProductResponse> productPage = this.productService
                .getProductsByKeyword(keyword, pageRequest)
                .map(ProductResponse::fromProduct);
//        logger.info("Đây là keyword: "+keyword);
        logger.info("HẢI ĐẸP TRAI CODE DẠO");
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> productResponses = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(productResponses)
                .totalPages(totalPages)
                .build());
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDTO productDTO
    ) throws Exception{
        try {
            return ResponseEntity.ok(
                    UpdateProductResponse.builder()
                            .product(this.productService.updateProduct(id, productDTO))
                            .message("Updated product successfully")
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    UpdateProductResponse.builder()
                            .message("Can not update with new product information, please check again")
                            .build()
            );
        }
    }
}
