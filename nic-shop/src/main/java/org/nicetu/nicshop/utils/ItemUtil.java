package org.nicetu.nicshop.utils;


import org.nicetu.nicshop.security.jwt.JwtAuthentication;

public interface ItemUtil {
    static Long getPrice(Item item, JwtAuthentication authentication) {
        if (authentication != null && item.getDiscount()) {
            return item.getDiscountPrice();
        }
        return item.getPrice();
    }

    static ProductStatus getStatus(Item item) {
        if (item.getCount() > 5) {
            return ProductStatus.AVAILABLE;
        }
        else if (item.getCount() >= 1) {
            return ProductStatus.LOW;
        }
        else {
            return ProductStatus.EMPTY;
        }
    }
}
