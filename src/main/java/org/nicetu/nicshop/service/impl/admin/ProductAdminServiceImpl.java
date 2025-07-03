package org.nicetu.nicshop.service.impl.admin;

import org.nicetu.nicshop.domain.Category;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.ItemProperty;
import org.nicetu.nicshop.domain.Property;
import org.nicetu.nicshop.mappers.ItemMapper;
import org.nicetu.nicshop.mappers.ItemPropertyMapper;
import org.nicetu.nicshop.repository.*;
import org.nicetu.nicshop.requests.admin.ItemPropertyRequest;
import org.nicetu.nicshop.requests.admin.ItemRequest;
import org.nicetu.nicshop.service.api.admin.ProductAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProductAdminServiceImpl implements ProductAdminService {

    private final ItemRepository productRepo;
    private final CategoryRepository categoryRepository;
    private final ItemPropertyRepository itemPropertyRepository;
    private final PropertyRepository propertyRepository;


    @Autowired
    public ProductAdminServiceImpl(ItemRepository productRepo,
                                   CategoryRepository categoryRepository,
                                   ItemPropertyRepository itemPropertyRepository,
                                   PropertyRepository propertyRepository
    ) {
        this.productRepo = productRepo;
        this.categoryRepository = categoryRepository;
        this.itemPropertyRepository = itemPropertyRepository;
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public Item addProduct(ItemRequest request) {
        ItemProperty itemProperty = ItemPropertyMapper.fromProductPropertyRequestToProductProperty(
                request.getItemPropertyRequest(),
                propertyRepository.findAllById(request.getItemPropertyRequest().getPropertyIds())
        );
        itemPropertyRepository.save(itemProperty);
        return productRepo.save(ItemMapper.fromProductRequestToProduct(
                request,
                itemProperty,
                categoryRepository.findById(request.getSubcategoryId()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"))
        ));
    }

    @Transactional
    public void updateProduct(ItemRequest request) {
        Item product = productRepo.findById(request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        Category subcategory = categoryRepository.findById(request.getSubcategoryId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));

        ItemPropertyRequest itemPropertyRequest = request.getItemPropertyRequest();
        List<Property> properties = propertyRepository.findAllById(itemPropertyRequest.getPropertyIds());
        ItemProperty itemProperty = ItemPropertyMapper
                .fromProductPropertyRequestToProductProperty(itemPropertyRequest, properties);
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
    public void deleteProduct(Long productId) {
        productRepo.delete(productRepo.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден")));
    }
}
