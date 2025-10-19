package com.ashtana.backend.DTO.RequestDTO;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
    private Double discount;
    private String brand;
    // private Long vendorId; // optional, admin only
}
