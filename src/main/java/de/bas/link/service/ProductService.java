package de.bas.link.service;

import de.bas.link.domain.dto.ProductView;
import de.bas.link.domain.enums.Condition;
import de.bas.link.domain.exception.MissingArgumentException;
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
import java.io.UncheckedIOException;
import java.util.Locale;

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
        if(productView.getTitle() == null) {
            throw new MissingArgumentException("title");
        }
        Product product = ProductMapper.INSTANCE.productViewToProduct(productView);
        product.setUserId(userId);
        Product savedProduct = productRepo.save(product);
        ProductView newProductView = ProductMapper.INSTANCE.productToProductView(savedProduct);
        return newProductView;
    }

    public String getProductCategories() {
        try {
            Reader reader = new InputStreamReader(categoriesFile.getInputStream(), UTF_8);
            return FileCopyUtils.copyToString(reader);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getProductConditions() {
        try {
            Reader reader = new InputStreamReader(conditionsFile.getInputStream(), UTF_8);
            return FileCopyUtils.copyToString(reader);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public ProductView getProductById(String productId) {
        Product product = productRepo.findById(new ObjectId(productId)).orElseThrow(() -> new NotFoundException("Id not found"));
        ProductView newProductView =ProductMapper.INSTANCE.productToProductView(product);
        return newProductView;

    }
}
