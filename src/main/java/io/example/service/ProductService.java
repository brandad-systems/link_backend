package io.example.service;

import io.example.domain.dto.ProductView;
import io.example.domain.enums.Condition;
import io.example.domain.model.Product;
import io.example.domain.model.User;
import io.example.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    @Transactional
    public ProductView create(ProductView productView) {
        // Get User via Auth
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
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

        // Return PruductView from saved Product
        ProductView newProductView = new ProductView();
        newProductView.setProductId(savedProduct.getProductId());
        newProductView.setTitle(savedProduct.getTitle());
        newProductView.setDescription(savedProduct.getDescription());
        newProductView.setPricePerDay(savedProduct.getPricePerDay());
        newProductView.setCondition(savedProduct.getCondition().toString());
        newProductView.setCategory(savedProduct.getCategory());
        newProductView.setPictureIds(savedProduct.getPictureIds());

        return newProductView;
    }
}
