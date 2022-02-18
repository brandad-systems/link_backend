package io.example.service;

import io.example.domain.dto.ProductView;
import io.example.domain.enums.Condition;
import io.example.domain.exception.MissingArgumentException;
import io.example.domain.model.Product;
import io.example.domain.model.User;
import io.example.repository.ProductRepo;
import io.example.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ProductView create(ProductView productView) {
        // Get User via Auth
        User user = Utils.getUser();

        if(productView.getTitle() == null) {
            throw new MissingArgumentException("title");
        }

        // Convert ProductView to Product
        Product product = new Product();
        product.setTitle(productView.getTitle());
        product.setDescription(productView.getDescription());
        product.setPricePerDay(productView.getPricePerDay());
        product.setCondition(Condition.valueOf(productView.getCondition().toUpperCase(Locale.ROOT)));
        product.setCategory(productView.getCategory());
        product.setPictureIds(productView.getPictureIds());
        product.setUserId(user.getId());

        Product savedProduct = productRepo.save(product);

        // Return ProductView from saved Product
        ProductView newProductView = new ProductView();
        newProductView.setId(savedProduct.getProductId().toString());
        newProductView.setTitle(savedProduct.getTitle());
        newProductView.setDescription(savedProduct.getDescription());
        newProductView.setPricePerDay(savedProduct.getPricePerDay());
        newProductView.setCondition(savedProduct.getCondition().toString());
        newProductView.setCategory(savedProduct.getCategory());
        newProductView.setPictureIds(savedProduct.getPictureIds());

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
}
