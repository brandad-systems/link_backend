package de.bas.link.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;
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

    @PositiveOrZero
    @NotNull
    private Double pricePerDay;

    @NotEmpty
    private String condition;

    private List<String> pictureIds;
}
