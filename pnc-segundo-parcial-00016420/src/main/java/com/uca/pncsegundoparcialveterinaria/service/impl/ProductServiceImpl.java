package com.uca.pncsegundoparcialveterinaria.service.impl;

import com.uca.pncsegundoparcialveterinaria.dto.ProductRequestDTO;
import com.uca.pncsegundoparcialveterinaria.dto.ProductResponseDTO;
import com.uca.pncsegundoparcialveterinaria.entity.Product;
import com.uca.pncsegundoparcialveterinaria.entity.ProductCategory;
import com.uca.pncsegundoparcialveterinaria.exception.BusinessRuleException;
import com.uca.pncsegundoparcialveterinaria.exception.ResourceNotFoundException;
import com.uca.pncsegundoparcialveterinaria.repository.ProductRepository;
import com.uca.pncsegundoparcialveterinaria.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        if (productRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BusinessRuleException("Ya hay un producto registrado con ese nombre: " + dto.getName());
        }
        Product product = buildProduct(dto);
        return toResponseDTO(productRepository.save(product));
    }

    @Override
    public List<ProductResponseDTO> findAll(ProductCategory category, Boolean available) {
        List<Product> products;

        if (category != null && available != null) {
            products = productRepository.findByCategoryAndAvailable(category, available);
        } else if (category != null) {
            products = productRepository.findByCategory(category);
        } else if (available != null) {
            products = productRepository.findByAvailable(available);
        } else {
            products = productRepository.findAll();
        }

        return products.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponseDTO(product);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Validar nombre único excluyendo el producto actual
        productRepository.findByNameIgnoreCase(dto.getName())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new BusinessRuleException("Ya existe un producto con el nombre: " + dto.getName());
                });

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setExpirationDate(dto.getExpirationDate());
        existing.setSupplier(dto.getSupplier());
        existing.setAvailable(resolveAvailability(dto.getStock(), existing.getAvailable()));
        existing.setRequiresPrescription(requiresPrescription(dto.getCategory()));

        return toResponseDTO(productRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // No se puede eliminar una vacuna disponible
        if (product.getCategory() == ProductCategory.VACCINE && Boolean.TRUE.equals(product.getAvailable())) {
            throw new BusinessRuleException(
                    "No es permitido eliminar una vacuna que se encuentra disponible"
            );
        }

        productRepository.delete(product);
    }

    private Product buildProduct(ProductRequestDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .available(dto.getStock() > 0)
                .requiresPrescription(requiresPrescription(dto.getCategory()))
                .expirationDate(dto.getExpirationDate())
                .supplier(dto.getSupplier())
                .build();
    }

    // MEDICINE y VACCINE requieren prescripción
    private boolean requiresPrescription(ProductCategory category) {
        return category == ProductCategory.MEDICINE || category == ProductCategory.VACCINE;
    }

    // Si stock llega a 0, available pasa a false automáticamente
    private boolean resolveAvailability(int newStock, boolean currentAvailable) {
        if (newStock == 0) return false;
        return currentAvailable;
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .available(product.getAvailable())
                .requiresPrescription(product.getRequiresPrescription())
                .expirationDate(product.getExpirationDate())
                .supplier(product.getSupplier())
                .build();
    }
}
