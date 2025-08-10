package com.github.gun2.productserviceapp.repository;

import com.github.gun2.productserviceapp.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 메모리를 사용하는 상품 저장소
 */
@Repository
public class ProductInMemoryRepository implements ProductRepository {
    private final AtomicLong idGenerator =  new AtomicLong(1);
    private final ConcurrentHashMap<Long, ProductDto> store = new ConcurrentHashMap<>();

    @Override
    public ProductDto save(ProductDto productDto) {
        boolean exists = productDto.getId() != null && store.get(productDto.getId()) != null;
        if (exists) {
            ProductDto product = ProductDto.builder()
                    .id(productDto.getId())
                    .price(productDto.getPrice())
                    .name(productDto.getName())
                    .updatedAt(LocalDateTime.now())
                    .build();
            store.put(product.getId(), product);
            return product;
        } else {
            ProductDto product = ProductDto.builder()
                    .id(idGenerator.getAndIncrement())
                    .price(productDto.getPrice())
                    .name(productDto.getName())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            store.put(product.getId(), product);
            return product;
        }
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public List<ProductDto> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public ProductDto findById(Long id) {
        return store.get(id);
    }
}
