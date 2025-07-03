package org.nicetu.nicshop.service.api.admin;

import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.requests.admin.AddPropertyRequest;
import org.nicetu.nicshop.requests.admin.PropertyRequest;

public interface PropertyAdminService {

    Property addProperty(AddPropertyRequest request);
    void updateProperty(AddPropertyRequest request);
    void updateItemProperty(Long id, PropertyRequest request);

}
