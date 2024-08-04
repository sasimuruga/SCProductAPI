package com.muruga.product.product_service_api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.muruga.product.product_service_api.domain.Product;
import com.muruga.product.product_service_api.dto.ProductDTO;
import com.muruga.product.product_service_api.dto.ProductMapping;
import com.muruga.product.product_service_api.exception.ProductAlreadyExistsException;
import com.muruga.product.product_service_api.exception.ProductNotFoundException;
import com.muruga.product.product_service_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapping productMapping;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Test Product");

        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Test Product");
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapping.productsToProductDTO(anyList())).thenReturn(List.of(productDTO));

        List<ProductDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
        verify(productMapping, times(1)).productsToProductDTO(anyList());
    }

    @Test
    void testGetAllProducts_EmptyList_ThrowsException() {
        when(productRepository.findAll()).thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, () -> productService.getAllProducts());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productMapping.productDtoToProduct(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productRepository, times(1)).findById(anyInt());
        verify(productMapping, times(1)).productDtoToProduct(any(Product.class));
    }

    @Test
    void testGetProductById_NotFound_ThrowsException() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1));
        verify(productRepository, times(1)).findById(anyInt());
    }

    @Test
    void testAddProduct() {
        when(productMapping.productDtoToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapping.productDtoToProduct(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.addProduct(productDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddProduct_DataIntegrityViolation_ThrowsException() {
        when(productMapping.productDtoToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.addProduct(productDTO));
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
