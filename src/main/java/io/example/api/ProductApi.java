package io.example.api;


import io.example.domain.dto.BookView;
import io.example.domain.dto.EditBookRequest;
import io.example.domain.dto.ProductView;
import io.example.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Product")
@RestController
@RequestMapping(path = "api/product")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @PostMapping
    public ProductView create(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid ProductView productView) {
                return productService.create(authHeader, productView);
    }
}
