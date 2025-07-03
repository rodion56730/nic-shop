package org.nicetu.nicshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.requests.admin.AddPropertyRequest;
import org.nicetu.nicshop.requests.admin.PropertyRequest;
import org.nicetu.nicshop.service.api.admin.PropertyAdminService;
import org.nicetu.nicshop.utils.validation.Marker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Свойства")
@RestController
@RequestMapping("/api/admin/property")
@PreAuthorize("hasAuthority('ADMIN')")
public class PropertyAdminController {

    private final PropertyAdminService propertyService;

    public PropertyAdminController(PropertyAdminService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    @Validated(Marker.onCreate.class)
    public ResponseEntity<Property> addProperty(@Valid @RequestBody AddPropertyRequest request) {
        return ResponseEntity.ok(propertyService.addProperty(request));
    }

    @PutMapping
    @Validated(Marker.onUpdate.class)
    public void updateProperty(@Valid @RequestBody AddPropertyRequest request) {
        propertyService.updateProperty(request);
    }

    @PutMapping("/{id}/updateProperty")
    public void updateProductProperty(@PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        propertyService.updateItemProperty(id, request);
    }

}
