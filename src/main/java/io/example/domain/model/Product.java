package io.example.domain.model;

import io.example.domain.enums.Condition;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "products")
@Data
public class Product {

    @Id
    private ObjectId productId;

    private String title;
    private String description;
    private String category ;
    private double pricePerDay;
    private Condition condition;
    private List<String> pictureIds;

    @CreatedBy
    private ObjectId userId;


}
