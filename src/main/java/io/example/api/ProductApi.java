package io.example.api;

import io.example.domain.dto.ProductView;
import io.example.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Product")
@RestController
@RequestMapping(path = "api/v1/product")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductView> create(@RequestBody @Valid ProductView productView) {
        ProductView createdProduct = productService.create(productView);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductCategories() {
        return new ResponseEntity<>(productService.getProductCategories(), HttpStatus.OK);
    }

    @GetMapping(value = "/conditions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductConditions() {
        return new ResponseEntity<>(productService.getProductConditions(), HttpStatus.OK);
    }
}
