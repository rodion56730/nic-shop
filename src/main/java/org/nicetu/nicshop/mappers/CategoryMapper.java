package org.nicetu.nicshop.mappers;

import org.nicetu.nicshop.domain.Category;
import org.nicetu.nicshop.requests.admin.CategoryRequest;

public interface CategoryMapper {

    static Category fromCategoryRequestToCategory(CategoryRequest request) {
        Category category = new Category();
        category.setId(request.getId());
        category.setName(request.getName());

        return category;
    }

}
