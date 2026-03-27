package com.simulation.offer_system.mapper;

import org.springframework.stereotype.Component;

import com.simulation.offer_system.dto.ProductDTO;
import com.simulation.offer_system.entity.Product;

@Component
public class ProductMapper {
	
	public ProductDTO toDto(Product product) {
        if (product == null) return null;
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setLastQuotedPrice(product.getLastQuotedPrice());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setLastQuotedPrice(dto.getLastQuotedPrice());
        return product;
    }

}
