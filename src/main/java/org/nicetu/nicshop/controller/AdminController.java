package org.nicetu.nicshop.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.domain.Category;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.requests.DeleteFeedbackRequest;
import org.nicetu.nicshop.requests.admin.*;
import org.nicetu.nicshop.service.AdminService;
import org.nicetu.nicshop.utils.validation.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Администратор")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Добавить категорию")
    @Validated(Marker.onCreate.class)
    @PostMapping("/category")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(adminService.addCategory(request));
    }

    @Operation(summary = "Редактировать категорию")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Категории не существует"))
    @Validated(Marker.onUpdate.class)
    @PutMapping("/category")
    public void updateCategory(@Valid @RequestBody CategoryRequest request) {
        adminService.updateCategory(request);
    }

    @Operation(summary = "Добавить подкатегорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Категории не существует
                    Родительской подкатегории не существует
                    """)
    })
    @Validated(Marker.onCreate.class)
    @PostMapping("/subcategory")
    public ResponseEntity<Category> addSubcategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(adminService.addSubcategory(request));
    }

    @Operation(summary = "Редактировать подкатегорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Категории не существует
                    Подкатегории не существует
                    Родительской подкатегории не существует
                    """)
    })
    @Validated(Marker.onUpdate.class)
    @PutMapping("/subcategory")
    public void updateSubcategory(@Valid @RequestBody SubcategoryRequest request) {
        adminService.updateSubcategory(request);
    }

    @Operation(description = "Перемещение подкатегорию в другую категорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Категории не существует
                    Подкатегории не существует
                    """)
    })
    @PutMapping("/moveSubcategoryToCategory")
    public void moveSubcategoryToCategory(@Valid @RequestBody MoveSubcategoryToCategoryRequest request) {
        adminService.moveSubcategoryToCategory(request);
    }

    @Operation(description = "Перемещение подкатегории внутри категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Подкатегории не существует
                    Родительской подкатегории не существует
                    """)
    })
    @PutMapping("/moveSubcategory")
    public void moveSubcategory(@Valid @RequestBody MoveSubcategoryRequest request) {
        adminService.moveSubcategory(request);
    }

    @Operation(description = "Добавить товар")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Подкатегории не существует"))
    @Validated(Marker.onCreate.class)
    @PostMapping("/product")
    public ResponseEntity<Item> addProduct(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(adminService.addProduct(request));
    }

    @Operation(description = "Отредактировать существующий товар")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Подкатегории не существует
                    Товара не существует
                    """)
    })
    @Validated(Marker.onUpdate.class)
    @PutMapping("/product")
    public void updateProduct(@Valid @RequestBody ItemRequest request) {
        adminService.updateProduct(request);
    }

    @Operation(description = "Добавить характеристику")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Неверный id набора свойств"))
    @Validated(Marker.onCreate.class)
    @PostMapping("/property")
    public ResponseEntity<Property> addProperty(@Valid @RequestBody AddPropertyRequest request) {
        return ResponseEntity.ok(adminService.addProperty(request));
    }

    @Operation(description = "Редактировать характеристику")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Неверный id набора свойств
                    Свойства не существует
                    """)
    })
    @Validated(Marker.onUpdate.class)
    @PutMapping("/property")
    public void updateProperty(@Valid @RequestBody AddPropertyRequest request) {
        adminService.updateProperty(request);
    }

    @Operation(description = "Редактировать характеристику товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = """
                    Свойства не существует
                    Товара не существует
                    """)
    })
    @PutMapping("/product/{id}/updateProperty")
    public void updateProductProperty(@PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        adminService.updateItemProperty(id, request);
    }

    @Operation(description = "Удалить товар")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Товара не существует"))
    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
    }

    @Operation(description = "Удалить категорию")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Категории не существует"))
    @DeleteMapping("/deleteCategory/{id}")
    public void deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
    }

    @Operation(description = "Удалить подкатегорию")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Подкатегории не существует"))
    @DeleteMapping("/deleteSubcategory/{id}")
    public void deleteSubcategory(@PathVariable Long id) {
        adminService.deleteSubcategory(id);
    }

    @Operation(description = "Удаление отзыва полностью")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Товар не существует"),
            @ApiResponse(responseCode = "404", description = """
                    Отзыва не существует
                    Товара не существует
                    """)
    })
    @DeleteMapping("/catalog/product/{id}/deleteFeedback")
    public void deleteFeedback(@Valid @RequestBody DeleteFeedbackRequest request, @PathVariable Long id) {
        adminService.deleteFeedback(request, id);
    }

    @Operation(description = "Удаление фотографий из отзыва")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Товар не существует"),
            @ApiResponse(responseCode = "404", description = """
                    Отзыва не существует
                    Товара не существует
                    """)
    })
    @DeleteMapping("/catalog/product/{id}/deletePhotosFeedback")
    public void deletePhotosFeedback(@Valid @RequestBody DeleteFeedbackRequest request, @PathVariable Long id) {
        adminService.deletePhotosFeedback(request, id);
    }

    @Operation(description = "Удаление комментария из отзыва")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Товар не существует"),
            @ApiResponse(responseCode = "404", description = """
                    Отзыва не существует
                    Товара не существует
                    """)
    })
    @PutMapping("/catalog/product/{id}/deleteCommentFeedback")
    public void deleteCommentFeedback(@Valid @RequestBody DeleteFeedbackRequest request, @PathVariable Long id) {
        adminService.deleteCommentFeedback(request, id);
    }
}
