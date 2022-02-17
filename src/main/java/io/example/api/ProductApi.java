package io.example.api;

import io.example.domain.dto.ProductView;
import io.example.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Product")
@RestController
@RequestMapping(path = "api/product")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductView> create(@RequestBody @Valid ProductView productView) {
        ProductView createdProduct = productService.create(productView);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
}
