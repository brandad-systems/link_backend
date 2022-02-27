package de.bas.link.repository;

import de.bas.link.domain.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//QuerydslPredicateExecutor<Product>
@Repository
public interface ProductRepo extends MongoRepository<Product, ObjectId> {

    List<Product> findByUserId(ObjectId userId);

}
