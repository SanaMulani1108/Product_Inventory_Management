package com.example.crudapi;

import com.example.crudapi.dto.ProductRequest;
import com.example.crudapi.dto.ProductResponse;
import com.example.crudapi.exception.DuplicateResourceException;
import com.example.crudapi.exception.ResourceNotFoundException;
import com.example.crudapi.model.Product;
import com.example.crudapi.repository.ProductRepository;
import com.example.crudapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Laptop Pro")
                .description("High performance laptop")
                .price(new BigDecimal("1299.99"))
                .stock(50)
                .category("Electronics")
                .active(true)
                .build();

        sampleRequest = ProductRequest.builder()
                .name("Laptop Pro")
                .description("High performance laptop")
                .price(new BigDecimal("1299.99"))
                .stock(50)
                .category("Electronics")
                .build();
    }

    @Test
    void createProduct_Success() {
        when(productRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Laptop Pro");
        assertThat(response.getPrice()).isEqualByComparingTo("1299.99");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_DuplicateName_ThrowsException() {
        when(productRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(sampleRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void getProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop Pro");
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteProduct_SoftDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        productService.deleteProduct(1L);

        assertThat(sampleProduct.getActive()).isFalse();
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductRequest updateRequest = ProductRequest.builder()
                .name("Laptop Pro Max")
                .description("Updated description")
                .price(new BigDecimal("1499.99"))
                .stock(30)
                .category("Electronics")
                .build();

        ProductResponse response = productService.updateProduct(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
