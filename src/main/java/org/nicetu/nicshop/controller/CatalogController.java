package org.nicetu.nicshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.dto.CatalogDTO;
import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.requests.FeedbackRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.service.api.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.validation.Valid;

@Tag(name = "Каталог")
@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(summary = "Получить все товары")
    @GetMapping()
    public ResponseEntity<CatalogDTO> getAllProducts(JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getAllProducts(authentication));
    }

    @Operation(summary = "Получить товар по id")
    @GetMapping("/product/{id}")
    public ResponseEntity<ItemDTO> getProduct(@PathVariable Long id, JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getProduct(id, authentication));
    }

    @Operation(summary = "Получить товар по id, отзывы упорядочены по возрастанию оценки")
    @GetMapping("/product/{id}/sortedAscending")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Товар не существует"))
    public ResponseEntity<ItemDTO> getProductFeedbacksSortedAscending(@PathVariable Long id, JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getProductFeedbacksSortedAscending(id, authentication));
    }

    @Operation(summary = "Получить товар по id, отзывы упорядочены по убыванию оценки")
    @GetMapping("/product/{id}/sortedDescending")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Товар не существует"))
    public ResponseEntity<ItemDTO> getProductFeedbacksSortedDescending(@PathVariable Long id, JwtAuthentication authentication) {
        return ResponseEntity.ok(catalogService.getProductFeedbacksSortedDescending(id, authentication));
    }

    @Operation(summary = "Добавление отзыва")
    @ApiResponses(@ApiResponse(responseCode = "400", description = "Некорректные данные"))
    @PostMapping("/product/{id}/newFeedback")
    public ResponseEntity<ItemDTO> addFeedback(@Valid @RequestBody FeedbackRequest request,
                                                  JwtAuthentication authentication,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.addFeedback(request, authentication, id));
    }

    @Operation(summary = "Получить все товары из категории")
    @GetMapping("/category/{id}")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Данной категории не существует"))
    public ResponseEntity<CatalogDTO> getSmartphones(JwtAuthentication authentication,
                                                     @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getAllProductsFromCategory(id, authentication));
    }

    @Operation(summary = "Получить все товары из подкатегории")
    @GetMapping("/subcategory/{id}")
    @ApiResponses(@ApiResponse(responseCode = "404", description = "Данной подкатегории не существует"))
    public ResponseEntity<CatalogDTO> getPortableSpeakers(JwtAuthentication authentication,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getAllProductsFromSubcategory(id, authentication));
    }

}
