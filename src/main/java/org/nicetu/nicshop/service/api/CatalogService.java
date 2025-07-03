package org.nicetu.nicshop.service.api;

import org.nicetu.nicshop.dto.CatalogDTO;
import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.requests.FeedbackRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;

public interface CatalogService {

    CatalogDTO getAllProducts(JwtAuthentication authentication);

    CatalogDTO getAllProductsFromCategory(Long categoryId, JwtAuthentication authentication);

    CatalogDTO getAllProductsFromSubcategory(Long subcategoryId, JwtAuthentication authentication);

    ItemDTO getProduct(Long id, JwtAuthentication authentication);

    ItemDTO getProductFeedbacksSortedAscending(Long id, JwtAuthentication authentication);

    ItemDTO getProductFeedbacksSortedDescending(Long id, JwtAuthentication authentication);

    ItemDTO addFeedback(FeedbackRequest request, JwtAuthentication authentication, Long id);
}
