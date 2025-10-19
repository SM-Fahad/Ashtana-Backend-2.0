package com.ashtana.backend.DTO.RequestDTO;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Long userId;
    private Long productId;
    private String comment;
    private Integer rating;
}
