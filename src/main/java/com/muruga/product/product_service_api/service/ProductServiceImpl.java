package com.muruga.product.product_service_api.service;

import com.muruga.product.product_service_api.domain.Product;
import com.muruga.product.product_service_api.dto.ProductDTO;
import com.muruga.product.product_service_api.dto.ProductMapping;
import com.muruga.product.product_service_api.exception.ProductAlreadyExistsException;
import com.muruga.product.product_service_api.exception.ProductNotFoundException;
import com.muruga.product.product_service_api.repository.ProductRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductMapping productMapping;
  private final ProductRepository productRepository;

  @Override
  public List<ProductDTO> getAllProducts() {
    List<Product> all = productRepository.findAll();
    if(all.isEmpty()){
      throw new ProductNotFoundException();
    }
    return productMapping.productsToProductDTO(all);
  }

  @Override
  public ProductDTO getProductById(int productId) {
    Product byId =
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    return productMapping.productDtoToProduct(byId);
  }

  @Override
  public ProductDTO addProduct(ProductDTO productDTO) {
    Product product = productMapping.productDtoToProduct(productDTO);
    try {
      Product save = productRepository.save(product);
      return productMapping.productDtoToProduct(save);
    } catch (DataIntegrityViolationException ex) {
      throw new ProductAlreadyExistsException();
    }
  }
}
