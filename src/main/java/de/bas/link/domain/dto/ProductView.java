package de.bas.link.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductView {
    private String productId;
    private String title;
    private String description;
    private String category;
    private double pricePerDay;
    private String condition;
    private List<String> pictureIds;
}
