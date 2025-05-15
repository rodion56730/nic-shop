package org.nicetu.nicshop.mappers;



import org.nicetu.nicshop.domain.ItemProperty;
import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.requests.admin.ItemPropertyRequest;

import java.util.List;

public interface ItemPropertyMapper {
    static ItemProperty fromProductPropertyRequestToProductProperty(ItemPropertyRequest request, List<Property> properties) {
        ItemProperty itemProperty = new ItemProperty();
        itemProperty.setId(request.getId());
        itemProperty.setProperties(properties);

        return itemProperty;
    }
}

