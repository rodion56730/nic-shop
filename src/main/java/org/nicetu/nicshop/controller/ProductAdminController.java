package org.nicetu.nicshop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.requests.admin.ItemRequest;
import org.nicetu.nicshop.requests.admin.PropertyRequest;
import org.nicetu.nicshop.service.api.admin.ProductAdminService;
import org.nicetu.nicshop.utils.validation.Marker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Товары")
@RestController
@RequestMapping("/api/admin/product")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductAdminController {

    private final ProductAdminService productService;

    public ProductAdminController(ProductAdminService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Validated(Marker.onCreate.class)
    public ResponseEntity<Item> addProduct(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(productService.addProduct(request));
    }

    @PutMapping
    @Validated(Marker.onUpdate.class)
    public void updateProduct(@Valid @RequestBody ItemRequest request) {
        productService.updateProduct(request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
