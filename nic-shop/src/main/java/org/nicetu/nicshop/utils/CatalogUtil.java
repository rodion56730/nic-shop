package org.nicetu.nicshop.utils;



import java.util.List;

public interface CatalogUtil {
    static String getInitials(User user) {
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    static void getProductsFromSubcategoryAndChilds(List<Item> products, List<Category> childSubcategories) {
        products.addAll(childSubcategories.stream()
                .flatMap(s -> s.getProducts().stream())
                .toList());

        for(Category subcategory : childSubcategories) {
            getProductsFromSubcategoryAndChilds(products, subcategory.getChildren());
        }
    }
}
