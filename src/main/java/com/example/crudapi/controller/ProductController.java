package com.example.crudapi.controller;

import com.example.crudapi.dto.ApiResponse;
import com.example.crudapi.dto.ProductRequest;
import com.example.crudapi.dto.ProductResponse;
import com.example.crudapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    // ─── POST /api/v1/products ───────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", created));
    }

    // ─── GET /api/v1/products ────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", products));
    }

    // ─── GET /api/v1/products/{id} ───────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product fetched successfully", product));
    }

    // ─── GET /api/v1/products/search?name=xxx ────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> results = productService.searchProducts(name, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results", results));
    }

    // ─── GET /api/v1/products/category/{category} ────────────────────────────
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategory(
            @PathVariable String category) {

        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(
                "Products in category: " + category, products));
    }

    // ─── GET /api/v1/products/price-range?min=10&max=100 ─────────────────────
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        List<ProductResponse> products = productService.getProductsByPriceRange(min, max);
        return ResponseEntity.ok(ApiResponse.success("Products in price range", products));
    }

    // ─── GET /api/v1/products/low-stock?threshold=5 ──────────────────────────
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStock(
            @RequestParam(defaultValue = "5") int threshold) {

        List<ProductResponse> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success(
                "Products with stock <= " + threshold, products));
    }

    // ─── GET /api/v1/products/stats/categories ───────────────────────────────
    @GetMapping("/stats/categories")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getCategoryStats() {
        Map<String, Long> stats = productService.getCategoryStats();
        return ResponseEntity.ok(ApiResponse.success("Category statistics", stats));
    }

    // ─── PUT /api/v1/products/{id} ───────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updated));
    }

    // ─── DELETE /api/v1/products/{id} ────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }

    // ─── PATCH /api/v1/products/{id}/restore ─────────────────────────────────
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<ProductResponse>> restoreProduct(@PathVariable Long id) {
        ProductResponse restored = productService.restoreProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product restored successfully", restored));
    }
}
