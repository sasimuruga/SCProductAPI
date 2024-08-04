package com.muruga.product.product_service_api;

import com.muruga.product.product_service_api.dto.ProductDTO;
import com.muruga.product.product_service_api.exception.ProductNotFoundException;
import com.muruga.product.product_service_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disables security filters
class ProductResourceIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProductService productService;

  private ProductDTO productDTO;

  @BeforeEach
  void setUp() {
    productDTO = new ProductDTO();
    productDTO.setId(1);
    productDTO.setName("Test Product");
  }

  @Test
  void testAddProduct() throws Exception {
    when(productService.addProduct(productDTO)).thenReturn(productDTO);

    mockMvc
        .perform(
            post("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Test Product\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Test Product"));
  }

  @Test
  void testGetAllProducts() throws Exception {
    when(productService.getAllProducts()).thenReturn(List.of(productDTO));

    mockMvc
        .perform(get("/v1/product/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Test Product"));
  }

  @Test
  void testGetProductById() throws Exception {
    when(productService.getProductById(anyInt())).thenReturn(productDTO);

    mockMvc
        .perform(get("/v1/product/{product-id}", 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Test Product"));
  }

  @Test
  void testGetProductById_NotFound() throws Exception {
    when(productService.getProductById(anyInt()))
        .thenThrow(new ProductNotFoundException());

    mockMvc
        .perform(get("/v1/product/{product-id}", 1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
