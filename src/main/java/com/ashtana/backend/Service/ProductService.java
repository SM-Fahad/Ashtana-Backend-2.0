package com.ashtana.backend.Service;

import com.ashtana.backend.DTO.RequestDTO.ProductRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.ProductResponseDTO;
import com.ashtana.backend.Entity.*;
import com.ashtana.backend.Enums.ProductStatus;
import com.ashtana.backend.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepository;
    private final ColorRepo colorRepository;
    private final SizeRepo sizeRepository;
    private final UserRepo userRepo;

    // -------------------- IMAGE SAVE UTILITY -------------------- //
    private final String uploadDir = "uploads/products/";

    private String saveImage(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            return "/uploads/products/" + fileName; // You can change this to a full URL later
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
    }

    // -------------------- CREATE PRODUCT (Single file) -------------------- //
    public ProductResponseDTO createProduct(ProductRequestDTO dto, MultipartFile imageFile) {
        Product product = buildProductFromDto(dto);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            product.setImageUrls(List.of(imageUrl));
        }

        Product saved = productRepo.save(product);
        return toDto(saved);
    }

    // -------------------- CREATE PRODUCT (Multiple files) -------------------- //
    public ProductResponseDTO createProduct(ProductRequestDTO dto, MultipartFile[] imageFiles) {
        Product product = buildProductFromDto(dto);

        if (imageFiles != null && imageFiles.length > 0) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    imageUrls.add(saveImage(file));
                }
            }
            product.setImageUrls(imageUrls);
        }

        Product saved = productRepo.save(product);
        return toDto(saved);
    }

    // -------------------- BUILD PRODUCT FROM DTO -------------------- //
    private Product buildProductFromDto(ProductRequestDTO dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        SubCategory subCategory = null;
        if (dto.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("SubCategory not found"));
        }

        Color color = null;
        if (dto.getColorId() != null) {
            color = colorRepository.findById(dto.getColorId())
                    .orElseThrow(() -> new EntityNotFoundException("Color not found"));
        }

        Size size = null;
        if (dto.getSizeId() != null) {
            size = sizeRepository.findById(dto.getSizeId())
                    .orElseThrow(() -> new EntityNotFoundException("Size not found"));
        }

        User user = null;
        if (dto.getUserId() != null) {
            user = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStock());
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setColor(color);
        product.setSize(size);
        product.setUser(user);
        product.setStatus(ProductStatus.PENDING);

        return product;
    }

    // -------------------- GET PRODUCT -------------------- //
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
        return toDto(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // -------------------- UPDATE PRODUCT -------------------- //
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto, MultipartFile[] imageFiles) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        SubCategory subCategory = null;
        if (dto.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("SubCategory not found"));
        }

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStockQuantity(dto.getStock());
        existing.setCategory(category);
        existing.setSubCategory(subCategory);

        // Update image files if provided
        if (imageFiles != null && imageFiles.length > 0) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    imageUrls.add(saveImage(file));
                }
            }
            existing.setImageUrls(imageUrls);
        }

        Product updated = productRepo.save(existing);
        return toDto(updated);
    }

    // -------------------- DELETE PRODUCT -------------------- //
    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
        productRepo.delete(product);
    }

    // -------------------- MAPPING -------------------- //
    public ProductResponseDTO toDto(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStockQuantity());
        dto.setImageUrls(product.getImageUrls());
        dto.setStatus(product.getStatus());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        dto.setSubCategoryName(product.getSubCategory() != null ? product.getSubCategory().getName() : null);
        dto.setColorName(product.getColor() != null ? product.getColor().getColorName() : null);
        dto.setSizeName(product.getSize() != null ? product.getSize().getSizeName() : null);
        return dto;
    }

    // ✅ Utility to save an image file (you can adjust the implementation)
//    private String saveImage(MultipartFile file) {
//        // Example: upload to local folder or cloud storage
//        // For now, just return a dummy URL
//        return "/uploads/" + file.getOriginalFilename();
//    }

    // ✅ Add or update product images
    public ProductResponseDTO addImages(Long productId, MultipartFile[] imageFiles) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        if (imageFiles != null && imageFiles.length > 0) {
            List<String> newImageUrls = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    newImageUrls.add(saveImage(file));
                }
            }

            // Merge old + new images
            if (product.getImageUrls() == null) {
                product.setImageUrls(new ArrayList<>());
            }
            product.getImageUrls().addAll(newImageUrls);
        }

        Product updated = productRepo.save(product);
        return toDto(updated);
    }
}
