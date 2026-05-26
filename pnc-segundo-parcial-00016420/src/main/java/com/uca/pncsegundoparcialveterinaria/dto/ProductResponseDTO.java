package com.uca.pncsegundoparcialveterinaria.dto;

import com.uca.pncsegundoparcialveterinaria.entity.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private Integer stock;
    private Boolean available;
    private Boolean requiresPrescription;
    private LocalDate expirationDate;
    private String supplier;
}
