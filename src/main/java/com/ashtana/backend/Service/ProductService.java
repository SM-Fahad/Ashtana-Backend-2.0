package com.ashtana.backend.Service;


import com.ashtana.backend.DTO.RequestDTO.ProductRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.ProductResponseDTO;
import com.ashtana.backend.Entity.Category;
import com.ashtana.backend.Entity.Product;
import com.ashtana.backend.Entity.User;
import com.ashtana.backend.Enums.ProductStatus;
import com.ashtana.backend.Repository.CategoryRepo;
import com.ashtana.backend.Repository.ProductRepo;
import com.ashtana.backend.Repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    // -------------------- Mapper -------------------- //
    public Product toEntity(ProductRequestDTO dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStock());
        product.setImageUrls(dto.getImageUrls());
        product.setCategory(category);
        product.setStatus(ProductStatus.PENDING);
        return product;
    }

    public ProductResponseDTO toDto(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStockQuantity());
        dto.setImageUrls(product.getImageUrls());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        dto.setStatus(product.getStatus());
        return dto;
    }

    // -------------------- Create Product -------------------- //
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = toEntity(dto, category);
        Product saved = productRepo.save(product);
        return toDto(saved);
    }

    // -------------------- Get Product -------------------- //
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
        return toDto(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // -------------------- Update Product -------------------- //
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto, Long vendorId) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStockQuantity(dto.getStock());
        existing.setImageUrls(dto.getImageUrls());
        existing.setCategory(category);

        Product updated = productRepo.save(existing);
        return toDto(updated);
    }

    // -------------------- Delete Product -------------------- //
    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        productRepo.delete(product);
    }

    // -------------------- Get Products By Vendor -------------------- //
//    public List<ProductResponseDTO> getProductsByVendor(Long vendorId) {
//        return productRepo.findAll().stream()
//                .filter(p -> p.getVendor() != null && p.getVendor().getId().equals(vendorId))
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
}
