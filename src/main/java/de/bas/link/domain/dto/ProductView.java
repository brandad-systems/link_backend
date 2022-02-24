package de.bas.link.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ProductView {
    private String productId;

    @NotEmpty
    private String title;

    @NotEmpty
    @Size(min = 10)
    private String description;

    @NotEmpty
    private String category;

    @Positive
    @NotNull
    private Double pricePerDay;

    @NotEmpty
    private String condition;

    private List<String> pictureIds;
}
