package org.nicetu.nicshop.service;


import org.json.JSONObject;
import org.nicetu.nicshop.domain.*;
import org.nicetu.nicshop.dto.BucketItemDTO;
import org.nicetu.nicshop.dto.CatalogDTO;
import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.dto.UserFeedbackDto;
import org.nicetu.nicshop.mappers.BucketItemMapper;
import org.nicetu.nicshop.mappers.CatalogMapper;
import org.nicetu.nicshop.mappers.ItemMapper;
import org.nicetu.nicshop.mappers.UserFeedbackMapper;
import org.nicetu.nicshop.repository.*;
import org.nicetu.nicshop.requests.FeedbackRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.utils.CatalogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.Cookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final CategoryRepository categoryRepository;
    private final ItemRepository productRepo;
    private final UserRepository userRepo;
    private final UserItemRepository userItemRepository;
    private final UserFeedbackRepository userFeedbackRepository;
    private final PhotoRepository photoRepository;
    private final CookieService cookieService;

    @Autowired
    public CatalogService(CategoryRepository categoryRepository,
                          ItemRepository productRepo,
                          UserRepository userRepo,
                          UserItemRepository userItemRepository,
                          UserFeedbackRepository userFeedbackRepository,
                          PhotoRepository photoRepository,
                          CookieService cookieService
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.userItemRepository = userItemRepository;
        this.userFeedbackRepository = userFeedbackRepository;
        this.photoRepository = photoRepository;
        this.cookieService = cookieService;
    }

    public CatalogDTO getAllProducts(JwtAuthentication authentication) {
        List<Item> products = productRepo.findAll();
        return getCatalogDto(authentication, products);
    }

    private CatalogDTO getCatalogDto(JwtAuthentication authentication, List<Item> products) {
        List<ItemDTO> productDtos = products.stream()
                .map(product -> {
                    List<UserFeedback> userFeedbacks = product.getUserFeedbacks();
                    List<UserFeedbackDto> userFeedbackDtos = UserFeedbackMapper.fromUserFeedbacksToDtos(userFeedbacks);
                    return ItemMapper.fromProductToDto(product, authentication, userFeedbackDtos);
                })
                .collect(Collectors.toList());

        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            List<BucketItem> userProducts = userItemRepository.findAllByUser(user);
            List<BucketItemDTO> userProductDtos = BucketItemMapper.fromUserProductsToDtos(userProducts, authentication);

            return CatalogMapper.fromProductDtosToCatalogDto(productDtos, userProductDtos, user);
        } else {
            Cookie[] cookies = cookieService.getAllCookies();
            List<BucketItemDTO> userProductDtos = new ArrayList<>();

            if (cookies == null) {
                return CatalogMapper.fromProductDtosToCatalogDto(productDtos, userProductDtos, null);
            }

            List<String> values = Arrays.stream(cookies)
                    .filter(c -> c.getName().contains("user_product_"))
                    .map(Cookie::getValue)
                    .toList();

            for (String value : values) {
                BucketItemDTO dto = new BucketItemDTO();
                JSONObject json = new JSONObject(URLDecoder.decode(value, StandardCharsets.UTF_8));
                dto.setPictureUrl((String) json.get("pictureUrl"));
                dto.setName((String) json.get("name"));
                dto.setPrice(((Number) json.get("price")).longValue());
                dto.setAmount(((Number) json.get("amount")).longValue());
                userProductDtos.add(dto);
            }

            return CatalogMapper.fromProductDtosToCatalogDto(productDtos, userProductDtos, null);
        }
    }

    public ItemDTO getProduct(Long id, JwtAuthentication authentication) {
        Item product = productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        List<UserFeedback> userFeedbacks = product.getUserFeedbacks();
        List<UserFeedbackDto> userFeedbackDtos = UserFeedbackMapper.fromUserFeedbacksToDtos(userFeedbacks);

        return ItemMapper.fromProductToDto(product, authentication, userFeedbackDtos);
    }

    public ItemDTO getProductFeedbacksSortedAscending(Long id, JwtAuthentication authentication) {
        Item product = productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        List<UserFeedback> userFeedbacks = product.getUserFeedbacks().stream()
                .sorted(Comparator.comparingLong(UserFeedback::getFeedback))
                .toList();
        List<UserFeedbackDto> userFeedbackDtos = UserFeedbackMapper.fromUserFeedbacksToDtos(userFeedbacks);

        return ItemMapper.fromProductToDto(product, authentication, userFeedbackDtos);
    }

    public ItemDTO getProductFeedbacksSortedDescending(Long id, JwtAuthentication authentication) {
        Item product = productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        List<UserFeedback> userFeedbacks = product.getUserFeedbacks().stream()
                .sorted(Comparator.comparingLong(UserFeedback::getFeedback).reversed())
                .toList();
        List<UserFeedbackDto> userFeedbackDtos = UserFeedbackMapper.fromUserFeedbacksToDtos(userFeedbacks);

        return ItemMapper.fromProductToDto(product, authentication, userFeedbackDtos);
    }

    @Transactional
    public ItemDTO addFeedback(FeedbackRequest request, JwtAuthentication authentication, Long id) {
        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            Item product = productRepo.findById(id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

            UserFeedback userFeedback = new UserFeedback();
            userFeedback.setUser(user);
            userFeedback.setComment(request.getComment());
            userFeedback.setItem(product);
            userFeedback.setFeedback(request.getFeedback());

            userFeedbackRepository.save(userFeedback);

            List<Photo> photos = new ArrayList<>();
            for(String picture: request.getPicturesUrls()) {
                Photo photo = new Photo();
                photo.setPictureUrl(picture);
                photo.setUserFeedback(userFeedback);
                photoRepository.save(photo);
                photos.add(photo);
            }
            userFeedback.setPhotos(photos);
            user.getUserFeedbacks().add(userFeedback);
        }

        return getProduct(id, authentication);
    }

    public CatalogDTO getAllProductsFromCategory(Long id, JwtAuthentication authentication) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной категории не существует"));
        List<Item> products = new ArrayList<>();
        CatalogUtil.getProductsFromSubcategoryAndChilds(products, category.getSubcategories());

        return getCatalogDto(authentication, products);
    }

    public CatalogDTO getAllProductsFromSubcategory(Long id, JwtAuthentication authentication) {
        Category subcategory = categoryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной подкатегории не существует"));
        List<Item> products = new ArrayList<>(subcategory.getProducts());
        CatalogUtil.getProductsFromSubcategoryAndChilds(products, subcategory.getChildren());

        return getCatalogDto(authentication, products);
    }
}
