package com.ashtana.backend.DTO.ResponseDTO;


import com.ashtana.backend.Enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private List<String> imageUrls;
    private String categoryName;
    private ProductStatus status;
}
