package org.nicetu.nicshop.service.impl.admin;

import org.nicetu.nicshop.domain.*;
import org.nicetu.nicshop.mappers.CategoryMapper;
import org.nicetu.nicshop.repository.*;
import org.nicetu.nicshop.requests.admin.*;
import org.nicetu.nicshop.service.api.admin.CategoryAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryAdminServiceImpl(
                            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category addCategory(CategoryRequest request) {
        return categoryRepository.save(CategoryMapper.fromCategoryRequestToCategory(request));
    }

    @Transactional
    public void updateCategory(CategoryRequest request) {
        Category category = categoryRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Transactional
    public Category addSubcategory(CategoryRequest request) {
        if (request.getParentId() != 0) {
            categoryRepository.findById(request.getParentId()).orElseThrow(() ->
                    new ResponseStatusException
                            (HttpStatus.NOT_FOUND, "Данной родительской подкатегории не существует"));
        }
        return categoryRepository.save(CategoryMapper.fromCategoryRequestToCategory(request));
    }

    @Transactional
    public void updateSubcategory(SubcategoryRequest request) {
        Category subcategory = categoryRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));
        subcategory.setParent(request.getParentId() == 0 ? null : categoryRepository.findById(request.getParentId())
                .orElseThrow(() ->
                        new ResponseStatusException
                                (HttpStatus.NOT_FOUND, "Данной родительской подкатегории не существует")));
        subcategory.setName(request.getName());
        categoryRepository.save(subcategory);
    }

    @Transactional
    public void moveSubcategoryToCategory(MoveSubcategoryToCategoryRequest request) {
        Category subcategory = categoryRepository.findById(request.getSubId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));
        Category category = categoryRepository.findById(request.getCatId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));
        subcategory.setId(category.getId());
        subcategory.setName(category.getName());
        categoryRepository.save(subcategory);
    }

    @Transactional
    public void moveSubcategory(MoveSubcategoryRequest request) {
        Category subcategory = categoryRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));
        subcategory.setParent(categoryRepository.findById(request.getDestId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной родительской подкатегории не существует")));
        categoryRepository.save(subcategory);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.delete(categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует")));
    }

    @Transactional
    public void deleteSubcategory(Long subcategoryId) {
        categoryRepository.delete(categoryRepository.findById(subcategoryId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует")));
    }
}
