package org.nicetu.nicshop.service.api.admin;

import org.nicetu.nicshop.domain.Category;
import org.nicetu.nicshop.requests.admin.CategoryRequest;
import org.nicetu.nicshop.requests.admin.MoveSubcategoryRequest;
import org.nicetu.nicshop.requests.admin.MoveSubcategoryToCategoryRequest;
import org.nicetu.nicshop.requests.admin.SubcategoryRequest;

public interface CategoryAdminService {
    Category addCategory(CategoryRequest request);
    void updateCategory(CategoryRequest request);
    Category addSubcategory(CategoryRequest request);
    void updateSubcategory(SubcategoryRequest request);
    void moveSubcategoryToCategory(MoveSubcategoryToCategoryRequest request);
    void moveSubcategory(MoveSubcategoryRequest request);
    void deleteCategory(Long categoryId);
    void deleteSubcategory(Long subcategoryId);
}
