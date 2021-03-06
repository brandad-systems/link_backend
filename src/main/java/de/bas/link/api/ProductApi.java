package de.bas.link.api;

import de.bas.link.domain.dto.ProductView;
import de.bas.link.domain.dto.RentalView;
import de.bas.link.service.ProductService;
import de.bas.link.utils.Utils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Tag(name = "Product")
@RestController
//@RequestMapping(path = "api/v1/product")
@RequestMapping({ "api/v1/product", "api/v1/products" })
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductView> create(@RequestBody @Valid ProductView productView) {
        ProductView createdProduct = productService.create(productView, Utils.getUser().getId());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", "api/v1/product/" + createdProduct.getProductId());
        return new ResponseEntity<>(createdProduct,responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductCategories() throws IOException {
        return new ResponseEntity<>(productService.getProductCategories(), HttpStatus.OK);
    }

    @GetMapping(value = "/conditions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductConditions() throws IOException {
        return new ResponseEntity<>(productService.getProductConditions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductView> getProductById(@PathVariable String id) {
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductView> > getProducts() {
        return new ResponseEntity<List<ProductView>>(productService.getProductByUserId(Utils.getUser().getId()),HttpStatus.OK);
    }
}
