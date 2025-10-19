package com.ashtana.backend.DTO.RequestDTO;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequestDTO {

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private List<String> imageUrls;
    private Long categoryId;

}
