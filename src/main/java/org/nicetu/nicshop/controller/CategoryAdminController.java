package org.nicetu.nicshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.domain.Category;
import org.nicetu.nicshop.requests.admin.CategoryRequest;
import org.nicetu.nicshop.requests.admin.MoveSubcategoryRequest;
import org.nicetu.nicshop.requests.admin.MoveSubcategoryToCategoryRequest;
import org.nicetu.nicshop.requests.admin.SubcategoryRequest;
import org.nicetu.nicshop.service.api.admin.CategoryAdminService;
import org.nicetu.nicshop.utils.validation.Marker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Категории")
@RestController
@RequestMapping("/api/admin/category")
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoryAdminController {

    private final CategoryAdminService categoryService;

    public CategoryAdminController(CategoryAdminService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Добавить категорию")
    @Validated(Marker.onCreate.class)
    @PostMapping
    public ResponseEntity<Category> addCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.addCategory(request));
    }

    @Operation(summary = "Редактировать категорию")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Категории не существует"))
    @Validated(Marker.onUpdate.class)
    @PutMapping
    public void updateCategory(@Valid @RequestBody CategoryRequest request) {
        categoryService.updateCategory(request);
    }

    @Operation(summary = "Добавить подкатегорию")
    @PostMapping("/subcategory")
    @Validated(Marker.onCreate.class)
    public ResponseEntity<Category> addSubcategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.addSubcategory(request));
    }

    @Operation(summary = "Редактировать подкатегорию")
    @PutMapping("/subcategory")
    @Validated(Marker.onUpdate.class)
    public void updateSubcategory(@Valid @RequestBody SubcategoryRequest request) {
        categoryService.updateSubcategory(request);
    }

    @Operation(description = "Перемещение подкатегории в другую категорию")
    @PutMapping("/moveSubcategoryToCategory")
    public void moveSubcategoryToCategory(@Valid @RequestBody MoveSubcategoryToCategoryRequest request) {
        categoryService.moveSubcategoryToCategory(request);
    }

    @Operation(description = "Перемещение подкатегории внутри категории")
    @PutMapping("/moveSubcategory")
    public void moveSubcategory(@Valid @RequestBody MoveSubcategoryRequest request) {
        categoryService.moveSubcategory(request);
    }

    @Operation(description = "Удалить категорию")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @Operation(description = "Удалить подкатегорию")
    @DeleteMapping("/subcategory/{id}")
    public void deleteSubcategory(@PathVariable Long id) {
        categoryService.deleteSubcategory(id);
    }
}
