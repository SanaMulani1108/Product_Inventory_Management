package com.example.crudapi.repository;

import com.example.crudapi.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find all active products (paginated)
    Page<Product> findByActiveTrue(Pageable pageable);

    // Find by category (case-insensitive)
    List<Product> findByCategoryIgnoreCaseAndActiveTrue(String category);

    // Search by name (contains, case-insensitive)
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    // Find products within price range
    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    // Check if name exists (for duplicate validation)
    boolean existsByNameIgnoreCase(String name);

    // Custom JPQL: find low-stock products
    @Query("SELECT p FROM Product p WHERE p.stock <= :threshold AND p.active = true")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    // Count by category
    @Query("SELECT p.category, COUNT(p) FROM Product p WHERE p.active = true GROUP BY p.category")
    List<Object[]> countByCategory();
}
