package com.ashtana.backend.DTO.ResponseDTO;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long id;
    private String productName;
    private Double pricePerItem;
    private Integer quantity;
    private Double totalPrice;
}
