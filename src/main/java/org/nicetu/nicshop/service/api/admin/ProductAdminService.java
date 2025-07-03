package org.nicetu.nicshop.service.api.admin;

import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.requests.admin.ItemRequest;

public interface ProductAdminService {
    Item addProduct(ItemRequest request);
    void updateProduct(ItemRequest request);
    void deleteProduct(Long productId);
}
