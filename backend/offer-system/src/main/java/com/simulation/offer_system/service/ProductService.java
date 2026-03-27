package com.simulation.offer_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.simulation.offer_system.dto.ProductDTO;
import com.simulation.offer_system.mapper.ProductMapper;
import com.simulation.offer_system.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;
    private final ProductMapper productMapper;

  
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

}
