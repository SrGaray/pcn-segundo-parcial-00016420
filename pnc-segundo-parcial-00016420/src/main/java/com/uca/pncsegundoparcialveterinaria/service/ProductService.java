package com.uca.pncsegundoparcialveterinaria.service;

import com.uca.pncsegundoparcialveterinaria.dto.ProductRequestDTO;
import com.uca.pncsegundoparcialveterinaria.dto.ProductResponseDTO;
import com.uca.pncsegundoparcialveterinaria.entity.ProductCategory;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO dto);

    List<ProductResponseDTO> findAll(ProductCategory category, Boolean available);

    ProductResponseDTO findById(Long id);

    ProductResponseDTO update(Long id, ProductRequestDTO dto);

    void delete(Long id);
}
