package de.bas.link.service;

import de.bas.link.domain.dto.ProductView;
import de.bas.link.domain.dto.RentalView;
import de.bas.link.domain.exception.NotFoundException;
import de.bas.link.domain.mapper.ProductMapper;
import de.bas.link.domain.model.Product;
import de.bas.link.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepo productRepo;

    @Value("classpath:productCategories.json")
    Resource categoriesFile;

    @Value("classpath:productConditions.json")
    Resource conditionsFile;

    @Transactional
    public ProductView create(ProductView productView, ObjectId userId) {
        Product product = ProductMapper.INSTANCE.productViewToProduct(productView);
        product.setUserId(userId);
        Product savedProduct = productRepo.save(product);
        return ProductMapper.INSTANCE.productToProductView(savedProduct);
    }

    public String getProductCategories() throws IOException {
        return readFileContent(categoriesFile);
    }

    public String getProductConditions() throws IOException {
        return readFileContent(conditionsFile);
    }

    public ProductView getProductById(String productId) {
        Product product = productRepo.findById(new ObjectId(productId)).orElseThrow(() -> new NotFoundException("Id not found"));
        return ProductMapper.INSTANCE.productToProductView(product);
    }

    private String readFileContent(Resource file) throws IOException {
        Reader reader = new InputStreamReader(file.getInputStream(), UTF_8);
        return FileCopyUtils.copyToString(reader);
    }

    public List<ProductView> getProductsByUserId(ObjectId userId) {
        List<Product> productList = productRepo.findByUserId(userId);
        return productList.stream().map(ProductMapper.INSTANCE::productToProductView).collect(Collectors.toList());
    }
}
