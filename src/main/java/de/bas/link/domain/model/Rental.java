package de.bas.link.domain.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "rental")
@Data
public class Rental {

    @Id
    private ObjectId rentalId;
    @DBRef
    private Product product;
    @DBRef
    private User user;

    private Date fromDate;
    private Date toDate;
}
