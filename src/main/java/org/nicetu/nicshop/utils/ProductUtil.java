package org.nicetu.nicshop.utils;


import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.ProductStatus;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;

import java.math.BigDecimal;

public interface ProductUtil {
    static Long getPrice(Item product, JwtAuthentication authentication) {
        if (authentication != null && product.getDiscount()) {
            return product.getDiscountPrice();
        }
        return product.getPrice();
    }

    static ProductStatus getStatus(Item product) {
        if (product.getCount() > 5) {
            return ProductStatus.AVAILABLE;
        }
        else if (product.getCount() >= 1) {
            return ProductStatus.LOW;
        }
        else {
            return ProductStatus.EMPTY;
        }
    }
}
