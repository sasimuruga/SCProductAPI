package com.muruga.product.product_service_api.dto;

import com.muruga.product.product_service_api.domain.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
unmappedTargetPolicy =  ReportingPolicy.IGNORE)
public interface ProductMapping {

  ProductMapping INSTANCE = Mappers.getMapper(ProductMapping.class);

  Product productDtoToProduct(ProductDTO productDTO);

  ProductDTO productDtoToProduct(Product product);

  List<ProductDTO> productsToProductDTO(List<Product> products);
}
