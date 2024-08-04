package com.muruga.product.product_service_api.service;


import com.muruga.product.product_service_api.dto.ProductDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(int productId);

    ProductDTO addProduct(ProductDTO productDTO);

}
