package com.example.crudapi.service;

import com.example.crudapi.dto.ProductRequest;
import com.example.crudapi.dto.ProductResponse;
import com.example.crudapi.exception.DuplicateResourceException;
import com.example.crudapi.exception.ResourceNotFoundException;
import com.example.crudapi.model.Product;
import com.example.crudapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());

        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Product with name '" + request.getName() + "' already exists");
        }

        Product product = mapToEntity(request);
        Product saved = productRepository.save(product);
        log.info("Product created with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    // ─── READ ALL (paginated) ─────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("Fetching all active products, page: {}", pageable.getPageNumber());
        return productRepository.findByActiveTrue(pageable)
                .map(this::mapToResponse);
    }

    // ─── READ BY ID ────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String name, Pageable pageable) {
        log.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable)
                .map(this::mapToResponse);
    }

    // ─── FILTER BY CATEGORY ───────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategoryIgnoreCaseAndActiveTrue(category)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ─── FILTER BY PRICE RANGE ────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
        log.info("Fetching products between price {} and {}", min, max);
        return productRepository.findByPriceBetweenAndActiveTrue(min, max)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ─── LOW STOCK ────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(int threshold) {
        log.info("Fetching products with stock <= {}", threshold);
        return productRepository.findLowStockProducts(threshold)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ─── CATEGORY STATS ───────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public Map<String, Long> getCategoryStats() {
        return productRepository.countByCategory()
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        // Check name conflict (allow same product to keep its name)
        if (!existing.getName().equalsIgnoreCase(request.getName())
                && productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Another product with name '" + request.getName() + "' already exists");
        }

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setCategory(request.getCategory());

        Product updated = productRepository.save(existing);
        log.info("Product updated: {}", updated.getId());
        return mapToResponse(updated);
    }

    // ─── SOFT DELETE ──────────────────────────────────────────────────────────
    public void deleteProduct(Long id) {
        log.info("Soft-deleting product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.setActive(false);
        productRepository.save(product);
    }

    // ─── RESTORE ──────────────────────────────────────────────────────────────
    public ProductResponse restoreProduct(Long id) {
        log.info("Restoring product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.setActive(true);
        return mapToResponse(productRepository.save(product));
    }

    // ─── MAPPERS ──────────────────────────────────────────────────────────────
    private Product mapToEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .active(true)
                .build();
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
