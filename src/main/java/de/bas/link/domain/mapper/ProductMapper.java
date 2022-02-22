package de.bas.link.domain.mapper;

import de.bas.link.domain.dto.ProductView;
import de.bas.link.domain.model.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_NULL;

@Mapper(uses = ProductTransform.class )
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    ProductView productToProductView(Product product);

    Product productViewToProduct(ProductView productView);
}
