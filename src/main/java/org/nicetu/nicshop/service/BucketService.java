package org.nicetu.nicshop.service;

import org.json.JSONObject;
import org.nicetu.nicshop.domain.BucketItem;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.dto.BucketItemDTO;
import org.nicetu.nicshop.mappers.BucketItemMapper;
import org.nicetu.nicshop.mappers.CartMapper;
import org.nicetu.nicshop.repository.ItemRepository;
import org.nicetu.nicshop.repository.UserItemRepository;
import org.nicetu.nicshop.repository.UserRepository;
import org.nicetu.nicshop.requests.AddItemRequest;
import org.nicetu.nicshop.requests.CartItemRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.utils.ItemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BucketService {
    private final UserItemRepository userItemRepository;
    private final UserRepository userRepo;
    private final ItemRepository productRepo;
    private final CookieService cookieService;
    private final EmailService emailService;


    @Autowired
    public BucketService(UserItemRepository userItemRepository,
                         UserRepository userRepo,
                         ItemRepository productRepo,
                         CookieService cookieService,
                         EmailService emailService
    ) {
        this.userItemRepository = userItemRepository;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.cookieService = cookieService;
        this.emailService = emailService;
    }

    public BucketDTO getUserProducts(JwtAuthentication authentication) {
        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            List<BucketItem> userProducts = userItemRepository.findAllByUser(user);
            List<BucketItemDTO> userProductDtos = BucketItemMapper.fromUserProductsToDtos(userProducts, authentication);

            return CartMapper.fromUserProductDTOListToCartDto(userProductDtos);
        } else {
            Cookie[] cookies = cookieService.getAllCookies();
            List<BucketItemDTO> userProductDtos = new ArrayList<>();

            if (cookies == null) {
                return CartMapper.fromUserProductDTOListToCartDto(userProductDtos);
            }

            List<String> values = Arrays.stream(cookies)
                    .filter(c -> c.getName().contains("user_product_"))
                    .map(Cookie::getValue)
                    .collect(Collectors.toList());

            for (String value : values) {
                BucketItemDTO dto = new BucketItemDTO();
                try {
                    String decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
                    JSONObject json = new JSONObject(decodedValue);
                    dto.setPictureUrl((String) json.get("pictureUrl"));
                    dto.setName((String) json.get("name"));
                    dto.setPrice(((Number) json.get("price")).longValue());
                    dto.setAmount(((Number) json.get("amount")).longValue());
                } catch (Exception e) {
                    // Обработка ошибок декодирования или парсинга JSON
                    e.printStackTrace();
                }
                userProductDtos.add(dto);
            }

            return CartMapper.fromUserProductDTOListToCartDto(userProductDtos);
        }
    }

    @Transactional
    public void reduceAmount(CartItemRequest request, JwtAuthentication authentication) {
        Item product = productRepo.findById(request.getProductId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            BucketItem userProduct = userItemRepository.findByItemAndUser(product, user).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар в корзине не найден"));

            Long amount = userProduct.getAmount();

            if (amount - 1 == 0) {
                userItemRepository.delete(userProduct);
            } else {
                userProduct.setAmount(amount - 1);
                userItemRepository.save(userProduct);
            }
        } else {
            Cookie cookie = cookieService.getCookie("user_product_" + product.getId());
            try {
                String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                JSONObject json = new JSONObject(decodedValue);
                long cookieValue = ((Number) json.get("amount")).longValue();

                if (cookieValue - 1 == 0) {
                    cookieService.deleteCookie("user_product_" + product.getId());
                } else {
                    json.put("amount", cookieValue - 1);
                    cookieService.setCookie("user_product_" + product.getId(), String.valueOf(json));
                }
            } catch (Exception e) {
                // Обработка ошибок декодирования или парсинга JSON
                e.printStackTrace();
            }

        }
    }

    @Transactional
    public void addAmount(CartItemRequest request, JwtAuthentication authentication) {
        Item product = productRepo.findById(request.getProductId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            BucketItem userProduct = userItemRepository.findByItemAndUser(product, user).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар в корзине не найден"));

            Long amount = userProduct.getAmount();

            if (product.getCount() < amount + 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Такого количества товара нет на складе");
            } else {
                userProduct.setAmount(amount + 1);
                userItemRepository.save(userProduct);
            }
        } else {
            Cookie cookie = cookieService.getCookie("user_product_" + product.getId());
            try {
                String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                JSONObject json = new JSONObject(decodedValue);
                long cookieValue = ((Number) json.get("amount")).longValue();

                if (product.getCount() < cookieValue + 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Такого количества товара нет на складе");
                } else {
                    json.put("amount", cookieValue + 1);
                    cookieService.setCookie("user_product_" + product.getId(), String.valueOf(json));
                }
            } catch (Exception e) {
                // Обработка ошибок декодирования или парсинга JSON
                e.printStackTrace();
            }

        }
    }

    @Transactional
    public void  addProduct(AddItemRequest request, JwtAuthentication authentication) {
        Item item = productRepo.findById(request.getProductId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (item.getCount() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Товара нет в наличии");
        }

        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));



            BucketItem userProduct = new BucketItem();
            userProduct.setUser(user);
            userProduct.setItem(item);
            userProduct.setAmount(1L);
            userItemRepository.save(userProduct);
        } else {
            JSONObject json = new JSONObject();
            json.put("pictureUrl", item.getImage());
            json.put("name", item.getName());
            json.put("price", item.getPrice());
            json.put("amount", 1L);
            cookieService.setCookie("user_product_" + item.getId(), String.valueOf(json));
        }
    }

    @Transactional
    public void deleteProduct(CartItemRequest request, JwtAuthentication authentication) {
        Item product = productRepo.findById(request.getProductId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            BucketItem userProduct = userItemRepository.findByItemAndUser(product, user).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар в корзине не найден"));

            userItemRepository.delete(userProduct);
        } else {
            cookieService.getCookie("user_product_" + product.getId());
            cookieService.deleteCookie("user_product_" + product.getId());
        }
    }

    //TODO J meter :)
    @Transactional
    public void buyProducts(JwtAuthentication authentication) {
        if (authentication != null) {
            User user = userRepo.findById(authentication.getUserId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

            List<BucketItem> userProducts = userItemRepository.findAllByUser(user);

            StringBuilder message = new StringBuilder();
            long fullPrice = 0L;

            for (BucketItem userProduct: userProducts) {
                Item product = userProduct.getItem();
                int productAmount = product.getCount();
                if (productAmount <= 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Продукта " + product.getName()
                            + " нет в наличии");
                }
                if (productAmount < userProduct.getAmount()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "В начилии только " + productAmount);
                }
                Long productPrice = ItemUtil.getPrice(product, authentication);
                Long userProductPrice = userProduct.getAmount();
                fullPrice += productPrice * userProductPrice;
                product.setCount((int) (productAmount - userProduct.getAmount()));
                message.append("Title: ")
                        .append(product.getName())
                        .append(". ")
                        .append("Amount: ")
                        .append(userProductPrice)
                        .append(". ")
                        .append("Price: ")
                        .append(productPrice)
                        .append("\n");
                productRepo.save(product);
            }
            message.append("Full price: ")
                    .append(fullPrice);

            emailService.sendSimpleEmail(user.getEmail(), "Shop Transaction", String.valueOf(message));
            userItemRepository.deleteAllByUser(user);
        } else {
            Cookie[] cookies = cookieService.getAllCookies();

            if (cookies == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Корзина пустая");
            }

            List<Cookie> sortCookies = Arrays.stream(cookies)
                    .filter(c -> c.getName().contains("user_product_"))
                    .collect(Collectors.toList());

            if (sortCookies.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Корзина пустая");
            }

            for (Cookie sortCookie : sortCookies) {
                Long productId = Long.valueOf(sortCookie.getName().replace("user_product_",""));
                Item product = productRepo.findById(productId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
                int productAmount = product.getCount();
                if (productAmount <= 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Продукта " + product.getName()
                            + " нет в наличии");
                }
                try {
                    String decodedValue = URLDecoder.decode(sortCookie.getValue(), StandardCharsets.UTF_8.name());
                    JSONObject json = new JSONObject(decodedValue);
                    long cookieValue = ((Number) json.get("amount")).longValue();
                    if (productAmount < cookieValue) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "В начилии только " + productAmount);
                    }
                    product.setCount((int) (productAmount - cookieValue));
                } catch (Exception e) {
                    // Обработка ошибок декодирования или парсинга JSON
                    e.printStackTrace();
                }

                productRepo.save(product);
                cookieService.deleteCookie(sortCookie.getName());
            }
        }
    }
}
