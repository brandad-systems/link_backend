package io.example.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductView {
    private String id;
    private String title;
    private String description;
    private String category;
    private double pricePerDay;
    private String condition;
    private List<String> pictureIds;
}
