package com.muruga.product.product_service_api.resource;

import com.muruga.product.product_service_api.dto.ProductDTO;
import com.muruga.product.product_service_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
@AllArgsConstructor
public class ProductResource {

  private final ProductService productService;

  @PostMapping
  @Operation(summary = "Save a product")
  public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
    ProductDTO productDTO1 = productService.addProduct(productDTO);
    return ResponseEntity.ok().body(productDTO1);
  }

  @GetMapping(value = "/all")
  @Operation(summary = "Get all products")
  public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping(value = "/{product-id}")
  @Operation(summary = "Get a product by id")
  public ProductDTO getProductById(@PathVariable("product-id") int productId) {
    return productService.getProductById(productId);
  }
}
