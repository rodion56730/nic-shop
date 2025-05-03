package org.nicetu.nicshop.mappers;



import org.nicetu.nicshop.domain.ProductProperty;
import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.requests.admin.ProductPropertyRequest;

import java.util.List;

public interface ProductPropertyMapper {
    static ProductProperty fromProductPropertyRequestToProductProperty(ProductPropertyRequest request, List<Property> properties) {
        ProductProperty productProperty = new ProductProperty();
        productProperty.setId(request.getId());
        productProperty.setProperties(properties);

        return productProperty;
    }
}

