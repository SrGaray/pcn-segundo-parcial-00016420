package com.uca.pncsegundoparcialveterinaria.repository;

import com.uca.pncsegundoparcialveterinaria.entity.Product;
import com.uca.pncsegundoparcialveterinaria.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Product> findByNameIgnoreCase(String name);
    List<Product> findByCategory(ProductCategory category);
    List<Product> findByAvailable(Boolean available);
    List<Product> findByCategoryAndAvailable(ProductCategory category, Boolean available);
}
