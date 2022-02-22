package de.bas.link.service;

import de.bas.link.domain.dto.ProductView;
import de.bas.link.domain.enums.Condition;
import de.bas.link.domain.model.Product;
import de.bas.link.repository.ProductRepo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private ProductService productService;

    @Mock
    private ProductRepo mockRepo;

    @BeforeEach
    public void setUp()
    {
        productService = new ProductService(mockRepo);
    }

    @Test
    public void createSavesProductEntity() {
        // Test if productService.create() executes productRepo.save once
        ProductView productView = new ProductView();
        productView.setTitle("Title");
        productView.setCategory("Category");
        productView.setCondition("NEU");
        productView.setPricePerDay(1.0);
        productView.setPictureIds(Arrays.asList("1", "2"));
        productView.setDescription("Description");

        Product product = new Product();
        Product created = new Product();

        product.setTitle(productView.getTitle());
        product.setDescription(productView.getDescription());
        product.setPricePerDay(productView.getPricePerDay());
        product.setCondition(Condition.valueOf(productView.getCondition().toUpperCase(Locale.ROOT)));
        product.setCategory(productView.getCategory());
        product.setPictureIds(productView.getPictureIds());
        product.setUserId(new ObjectId("61fcdc3c030a75799143988b"));

        created.setTitle(productView.getTitle());
        created.setDescription(productView.getDescription());
        created.setPricePerDay(productView.getPricePerDay());
        created.setCondition(Condition.valueOf(productView.getCondition().toUpperCase(Locale.ROOT)));
        created.setCategory(productView.getCategory());
        created.setPictureIds(productView.getPictureIds());
        created.setUserId(new ObjectId("61fcdc3c030a75799143988b"));
        created.setProductId(new ObjectId("620c9d952499a0058fce5665"));

        when(mockRepo.save(product)).thenReturn(created);
        productService.create(productView, new ObjectId("61fcdc3c030a75799143988b"));
        verify(mockRepo, times(1)).save(product);
    }
}
