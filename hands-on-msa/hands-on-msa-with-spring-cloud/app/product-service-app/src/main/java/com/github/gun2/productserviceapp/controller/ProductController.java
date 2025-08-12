package com.github.gun2.productserviceapp.controller;

import com.github.gun2.productservice.dto.ProductDto;
import com.github.gun2.productserviceapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 상품 컨트롤러
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> findAll() {
        List<ProductDto> dtoList = productService.findAll();
        return ResponseEntity.ok(dtoList);
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> find(
            @PathVariable(name = "id") Long id
    ) {
        ProductDto dto = productService.findById(id);
        return ResponseEntity.ok(dto);
    }



    @PostMapping("/products")
    public ResponseEntity<ProductDto> create(
            @Validated @RequestBody ProductDto.CreateRequest createRequest
    ) {
        ProductDto dto = productService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(dto);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> update(
            @Validated @RequestBody ProductDto.UpdateRequest updateRequest,
            @PathVariable(name = "id") Long id
    ) {
        ProductDto dto = productService.update(updateRequest, id);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") Long id
    ) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }


}
