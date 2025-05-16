package org.nicetu.nicshop.service;



import org.nicetu.nicshop.domain.*;
import org.nicetu.nicshop.mappers.CategoryMapper;
import org.nicetu.nicshop.mappers.ItemMapper;
import org.nicetu.nicshop.mappers.ItemPropertyMapper;
import org.nicetu.nicshop.repository.*;
import org.nicetu.nicshop.requests.DeleteFeedbackRequest;
import org.nicetu.nicshop.requests.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdminService {
    private final ItemRepository productRepo;
    private final CategoryRepository categoryRepository;
    private final ItemPropertyRepository itemPropertyRepository;
    private final PropertyRepository propertyRepository;
    private final PhotoRepository photoRepository;
    private final UserFeedbackRepository userFeedbackRepository;

    @Autowired
    public AdminService(ItemRepository productRepo,
                        CategoryRepository categoryRepository,
                        ItemPropertyRepository itemPropertyRepository,
                        PropertyRepository propertyRepository,
                        PhotoRepository photoRepository,
                        UserFeedbackRepository userFeedbackRepository
    ) {
        this.productRepo = productRepo;
        this.categoryRepository = categoryRepository;
        this.itemPropertyRepository = itemPropertyRepository;
        this.propertyRepository = propertyRepository;
        this.photoRepository = photoRepository;
        this.userFeedbackRepository = userFeedbackRepository;
    }

    @Transactional
    public Category addCategory(CategoryRequest request) {
        Category category = CategoryMapper.fromCategoryRequestToCategory(request);
        categoryRepository.save(category);

        return category;
    }

    @Transactional
    public void updateCategory(CategoryRequest request) {
        Long categoryId = request.getId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));

        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Transactional
    public Category addSubcategory(CategoryRequest request) {
        Long categoryId = request.getId();
        Long parentId = request.getParentId();
        Category parentSubcategory = null;

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));

        if (parentId != 0) {
            parentSubcategory = categoryRepository.findById(parentId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной родительской подкатегории не существует"));
        }

        Category subcategory = CategoryMapper.fromCategoryRequestToCategory(request);
        categoryRepository.save(subcategory);

        return subcategory;
    }

    @Transactional
    public void updateSubcategory(SubcategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Long parentId = request.getParentId();
        Category parentSubcategory = null;

        Category subcategory = categoryRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));

        if (parentId != 0) {
            parentSubcategory = categoryRepository.findById(parentId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной родительской подкатегории не существует"));
        }

        subcategory.setParent(parentSubcategory);
        subcategory.setName(request.getName());
        categoryRepository.save(subcategory);
    }

    @Transactional
    public void moveSubcategoryToCategory(MoveSubcategoryToCategoryRequest request) {
        Long subId = request.getSubId();
        Long catId = request.getCatId();

        Category subcategory = categoryRepository.findById(subId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));

        subcategory.setId(category.getId());
        subcategory.setName(category.getName());
        categoryRepository.save(subcategory);
    }

    @Transactional
    public void moveSubcategory(MoveSubcategoryRequest request) {
        Long id = request.getId();
        Long destId = request.getDestId();

        Category subcategory = categoryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        Category destSubcategory = categoryRepository.findById(destId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной родительской подкатегории не существует"));

        subcategory.setParent(destSubcategory);
        categoryRepository.save(subcategory);
    }

    @Transactional
    public Item addProduct(ItemRequest request) {
        Category subcategory = categoryRepository.findById(request.getSubcategoryId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        ItemPropertyRequest itemPropertyRequest = request.getItemPropertyRequest();
        List<Property> properties = propertyRepository.findAllById(itemPropertyRequest.getPropertyIds());
        ItemProperty itemProperty = ItemPropertyMapper.fromProductPropertyRequestToProductProperty(itemPropertyRequest, properties);
        itemPropertyRepository.save(itemProperty);

        Item product = ItemMapper.fromProductRequestToProduct(request, itemProperty, subcategory);
        productRepo.save(product);

        return product;
    }

    @Transactional
    public void updateProduct(ItemRequest request) {
        Item product = productRepo.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        Category subcategory = categoryRepository.findById(request.getSubcategoryId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        ItemPropertyRequest itemPropertyRequest = request.getItemPropertyRequest();
        List<Property> properties = propertyRepository.findAllById(itemPropertyRequest.getPropertyIds());
        ItemProperty itemProperty = ItemPropertyMapper.fromProductPropertyRequestToProductProperty(itemPropertyRequest, properties);
        itemPropertyRepository.save(itemProperty);

        product.setImage(request.getPictureUrl());
        product.setCategory(subcategory);
        product.setItemProperty(itemProperty);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCount(Math.toIntExact(request.getAmount()));
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setDiscount(request.getDiscount());

        productRepo.save(product);
    }

    @Transactional
    public Property addProperty(AddPropertyRequest request) {
        Property property = new Property();

        ItemProperty itemProperty = itemPropertyRepository.findById(request.getProdPropId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный id набора свойств"));

        property.setName(request.getName());
        property.setValue(request.getValue());
        property.setItemProperty(itemProperty);
        propertyRepository.save(property);

        return property;
    }

    @Transactional
    public void updateProperty(AddPropertyRequest request) {
        ItemProperty itemProperty = itemPropertyRepository.findById(request.getProdPropId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный id набора свойств"));

        Property property = propertyRepository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Свойства не существует"));

        property.setName(request.getName());
        property.setValue(request.getValue());
        property.setItemProperty(itemProperty);
        propertyRepository.save(property);
    }

    @Transactional
    public void updateItemProperty(Long id, PropertyRequest request) {
        productRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        Property property = propertyRepository.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Свойство не найдено"));

        property.setName(request.getName());
        property.setValue(request.getValue());

        propertyRepository.save(property);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Item product = productRepo.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        productRepo.delete(product);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));

        categoryRepository.delete(category);
    }

    @Transactional
    public void deleteSubcategory(Long subcategoryId) {
        Category subcategory = categoryRepository.findById(subcategoryId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        categoryRepository.delete(subcategory);
    }

    @Transactional
    public void deleteFeedback(DeleteFeedbackRequest request, Long id) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        UserFeedback userFeedback = userFeedbackRepository.findById(request.getFeedbackId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден"));

        userFeedbackRepository.delete(userFeedback);
    }

    @Transactional
    public void deletePhotosFeedback(DeleteFeedbackRequest request, Long id) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        UserFeedback userFeedback = userFeedbackRepository.findById(request.getFeedbackId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден"));

        List<Photo> photos = photoRepository.getAllByUserFeedback(userFeedback);
        photoRepository.deleteAll(photos);
    }

    @Transactional
    public void deleteCommentFeedback(DeleteFeedbackRequest request, Long id) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        UserFeedback userFeedback = userFeedbackRepository.findById(request.getFeedbackId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден"));

        userFeedback.setComment(null);

        userFeedbackRepository.save(userFeedback);
    }
}
