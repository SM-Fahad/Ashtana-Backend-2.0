package com.ashtana.backend.Controller;

import com.ashtana.backend.DTO.RequestDTO.ProductRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.ProductResponseDTO;
import com.ashtana.backend.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ✅ CREATE PRODUCT (single file upload)
    @PostMapping("/single")
    public ResponseEntity<ProductResponseDTO> createProductSingle(
            @Valid @RequestPart("product") ProductRequestDTO productRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        ProductResponseDTO response = productService.createProduct(productRequest, imageFile);
        return ResponseEntity.status(201).body(response);
    }

    // ✅ CREATE PRODUCT (multiple file upload)
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ProductResponseDTO> createProductMultiple(
//            @Valid @RequestPart("product") ProductRequestDTO productRequest,
//            @RequestPart(value = "imageFiles", required = false) MultipartFile[] imageFiles) {
//
//        ProductResponseDTO response = productService.createProduct(productRequest, imageFiles);
//        return ResponseEntity.status(201).body(response);
//    }

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ProductResponseDTO> createProductMultiple(
//            @RequestPart("product") ProductResponseDTO productRequest,
//            @RequestPart(value = "images", required = false) MultipartFile[] imageFiles) {
//
//        ProductResponseDTO savedProduct = productService.createProduct(productRequest, imageFiles);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
//    }

    @PostMapping(value = "/multiple", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDTO> createProductJson(@RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO response = productService.createProduct(productRequest, (MultipartFile) null);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping(value = "/upload-images/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> uploadImages(
            @PathVariable Long productId,
            @RequestPart("imageFiles") MultipartFile[] imageFiles) {

        ProductResponseDTO response = productService.addImages(productId, imageFiles);
        return ResponseEntity.ok(response);
    }


    // ✅ GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // ✅ GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // ✅ UPDATE PRODUCT (with multiple file upload)
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart("product") ProductRequestDTO productRequest,
            @RequestPart(value = "imageFiles", required = false) MultipartFile[] imageFiles) {

        ProductResponseDTO updated = productService.updateProduct(id, productRequest, imageFiles);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
