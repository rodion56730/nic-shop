package org.nicetu.nicshop.service.impl.admin;

import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.repository.*;
import org.nicetu.nicshop.requests.admin.AddPropertyRequest;
import org.nicetu.nicshop.requests.admin.PropertyRequest;
import org.nicetu.nicshop.service.api.admin.PropertyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PropertyAdminServiceImpl implements PropertyAdminService {

    private final ItemRepository productRepo;
    private final ItemPropertyRepository itemPropertyRepository;
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyAdminServiceImpl(ItemRepository productRepo,
                                    ItemPropertyRepository itemPropertyRepository,
                                    PropertyRepository propertyRepository
    ) {
        this.productRepo = productRepo;
        this.itemPropertyRepository = itemPropertyRepository;
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public Property addProperty(AddPropertyRequest request) {
        Property property = new Property();
        property.setName(request.getName());
        property.setValue(request.getValue());
        property.setItemProperty(itemPropertyRepository.findById(request.getProdPropId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный id набора свойств")));
        return propertyRepository.save(property);
    }

    @Transactional
    public void updateProperty(AddPropertyRequest request) {
        Property property = propertyRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Свойства не существует"));
        property.setName(request.getName());
        property.setValue(request.getValue());
        property.setItemProperty(itemPropertyRepository.findById(request.getProdPropId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный id набора свойств")));
        propertyRepository.save(property);
    }

    @Transactional
    public void updateItemProperty(Long id, PropertyRequest request) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        Property property = propertyRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Свойство не найдено"));
        property.setName(request.getName());
        property.setValue(request.getValue());
        propertyRepository.save(property);
    }

}
