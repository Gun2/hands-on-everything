package com.github.gun2.productserviceapp.repository;

import com.github.gun2.productservice.dto.ProductDto;

import java.util.List;

public interface ProductRepository {
    ProductDto save(ProductDto productDto);

    void delete(Long id);

    List<ProductDto> findAll();

    ProductDto findById(Long id);
}
