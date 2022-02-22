package de.bas.link.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bas.link.domain.dto.ProductView;
import de.bas.link.service.ProductService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static de.bas.link.util.JsonHelper.fromJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("ada.lovelace@nix.io")
public class TestProductsApi {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private final ProductView productView = new ProductView();

    @BeforeEach
    public void setUp() {
        this.productView.setTitle("My Title");
        this.productView.setDescription("My Description very long");
        this.productView.setCategory("Electronics/Audio");
        this.productView.setCondition("GUT");
        this.productView.setPricePerDay(55.0);
        this.productView.setPictureIds(new ArrayList<>());
    }

    @Test
    public void createProductSucceedsWhenAllFieldsAreCorrect() throws Exception {
        //given
        ProductView created = productView;
        created.setProductId("SOMEID");
        when(productService.create(any(ProductView.class), any(ObjectId.class))).thenReturn(created);

        //when
        MvcResult createResult = mockMvc.perform(post("/api/v1/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productView)))
            .andExpect(status().isCreated())
            .andReturn();

        verify(productService, times(1)).create(any(ProductView.class), any(ObjectId.class));
        ProductView createdProductView = fromJson(objectMapper, createResult.getResponse().getContentAsString(), ProductView.class);
        assertNotNull(createdProductView.getProductId(), "Product id must not be null!");
    }

    @Test
    public void createProductFailsWhenNoTitleIsProvided() throws Exception {
        productView.setTitle(null);

        mockMvc.perform(post("/api/v1/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productView)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductFailsWhenNoDescriptionIsProvided() throws Exception {
        productView.setDescription(null);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productView)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductFailsWhenShortDescriptionIsProvided() throws Exception {
        productView.setDescription("123456789");

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productView)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductFailsWhenNoCategoryIsProvided() throws Exception {
        productView.setCategory(null);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productView)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductFailsWhenNoConditionIsProvided() throws Exception {
        productView.setCondition(null);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productView)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductFailsWhenNoPriceIsProvided() throws Exception {
        productView.setPricePerDay(null);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productView)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProductFailsWhenNegativePriceIsProvided() throws Exception {
        productView.setPricePerDay(-1.0);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productView)))
                .andExpect(status().isBadRequest());
    }
}
