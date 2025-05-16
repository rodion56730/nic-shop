package org.nicetu.nicshop.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.dto.CatalogDTO;
import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.requests.FeedbackRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Каталог")
@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(summary = "Получить все товары")
    @GetMapping("/catalog")
    public ResponseEntity<CatalogDTO> getAllProducts(JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getAllProducts(authentication));
    }

    @Operation(summary = "Получить товар по id")
    @GetMapping("/catalog/product/{id}")
    //@ApiResponses(re = 404,)
    public ResponseEntity<ItemDTO> getProduct(@PathVariable Long id, JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getProduct(id, authentication));
    }

    @Operation(summary = "Получить товар по id, отзывы упорядочены по возрастанию оценки")
    @GetMapping("/catalog/product/{id}/sortedAscending")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Товар не существует"))
    public ResponseEntity<ItemDTO> getProductFeedbacksSortedAscending(@PathVariable Long id, JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getProductFeedbacksSortedAscending(id, authentication));
    }

    @Operation(summary = "Получить товар по id, отзывы упорядочены по убыванию оценки")
    @GetMapping("/catalog/product/{id}/sortedDescending")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Товар не существует"))
    public ResponseEntity<ItemDTO> getProductFeedbacksSortedDescending(@PathVariable Long id, JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getProductFeedbacksSortedDescending(id, authentication));
    }

    @Operation(summary = "Добавление отзыва")
    @ApiResponses(@ApiResponse(responseCode = "400", description = "Некорректные данные"))
    @PostMapping("/catalog/product/{id}/addFeedback")
    public ResponseEntity<ItemDTO> addFeedback(@Valid @RequestBody FeedbackRequest request,
                                                  JwtAuthentication authentication,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.addFeedback(request, authentication, id));
    }

    @Operation(summary = "Получить все товары из категории")
    @GetMapping("/catalog/category/{id}")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Данной категории не существует"))
    public ResponseEntity<CatalogDTO> getSmartphones(JwtAuthentication authentication,
                                                     @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getAllProductsFromCategory(id, authentication));
    }

    @Operation(summary = "Получить все товары из подкатегории")
    @GetMapping("/catalog/subcategory/{id}")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Данной подкатегории не существует"))
    public ResponseEntity<CatalogDTO> getPortableSpeakers(JwtAuthentication authentication,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getAllProductsFromSubcategory(id, authentication));
    }
}
