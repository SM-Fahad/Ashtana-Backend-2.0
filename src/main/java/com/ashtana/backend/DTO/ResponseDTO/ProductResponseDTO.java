package com.ashtana.backend.DTO.ResponseDTO;

import com.My.E_CommerceApp.Enum.ProductStatus;
import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private Double discount;   // optional
    private String brand;      // optional
    private String categoryName;
    private ProductStatus status;
    private Long vendorId;
    private String vendorName;
}
