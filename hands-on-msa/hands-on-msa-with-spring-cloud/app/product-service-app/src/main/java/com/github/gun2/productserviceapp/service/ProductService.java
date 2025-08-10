package com.github.gun2.productserviceapp.service;

import com.github.gun2.productserviceapp.dto.ProductDto;
import com.github.gun2.productserviceapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;

        List<ProductDto.CreateRequest> productCreateRequestList = List.of(
                ProductDto.CreateRequest.builder()
                        .name("진라면 순한맛")
                        .price(1000L)
                        .build(),
                ProductDto.CreateRequest.builder()
                        .name("스낵면")
                        .price(1000L)
                        .build(),
                ProductDto.CreateRequest.builder()
                        .name("프링글스 양파맛")
                        .price(2000L)
                        .build()
        );
        productCreateRequestList.forEach(this::create);
    }

    public ProductDto create(ProductDto.CreateRequest createRequest) {
        return productRepository.save(
                ProductDto.builder()
                        .name(createRequest.getName())
                        .price(createRequest.getPrice())
                .build()
        );
    }

    public ProductDto update(ProductDto.UpdateRequest updateRequest, Long id) {
        return productRepository.save(
                ProductDto.builder()
                        .id(id)
                        .name(updateRequest.getName())
                        .price(updateRequest.getPrice())
                        .build()
        );
    }

    public void delete(Long id) {
        productRepository.delete(id);
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll();
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id);
    }


}
