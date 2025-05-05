package org.nicetu.nicshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.nicetu.nicshop.controller.AuthController;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.RefreshToken;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.JwtResponseDto;
import org.nicetu.nicshop.repository.BucketRepository;
import org.nicetu.nicshop.repository.ItemRepository;
import org.nicetu.nicshop.repository.UserProductRepo;
import org.nicetu.nicshop.repository.UserRepository;
import org.nicetu.nicshop.requests.AddProductRequest;
import org.nicetu.nicshop.requests.AuthRequest;
import org.nicetu.nicshop.requests.CartProductRequest;
import org.nicetu.nicshop.requests.RegisterRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.security.jwt.JwtProvider;
import org.nicetu.nicshop.security.jwt.JwtUtils;
import org.nicetu.nicshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.LongStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class NicShopApplicationTests {
    private final String userEmail = "test@gmail.com";
    private final MockMvc mockMvc;
    private final ItemRepository productRepo;
    private final UserProductRepo bucketRepository;
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public NicShopApplicationTests(
            MockMvc mockMvc,
            ItemRepository productRepo,
            UserProductRepo bucketRepository,
            AuthService authService,
            JwtProvider jwtProvider,
            UserRepository userRepo
    ) {
        this.mockMvc = mockMvc;
        this.productRepo = productRepo;
        this.bucketRepository = bucketRepository;
        this.authService = authService;
        this.jwtProvider = jwtProvider;
        this.userRepo = userRepo;

    }

    private void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserPassword("123");
        registerRequest.setUserEmail(userEmail);
        authService.registerUser(registerRequest);
    }


    public JwtAuthentication authUser() {
        AuthRequest request = new AuthRequest();
        request.setUserEmail(userEmail);
        request.setUserPassword("123");
        JwtResponseDto jwtResponseDto = authService.authenticateUser(request);
        Claims claims = jwtProvider.getAccessClaims(jwtResponseDto.getAccessToken());
        return JwtUtils.getAuthentication(claims);
    }

    private void logoutUser() {
        authService.deleteAllById(1L);
    }

    @Test
    void checkEmptyCart() throws Exception {
        mockMvc
                .perform(get("/api/cart").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void notExistingProduct() throws Exception {
        mockMvc
                .perform(get("/api/catalog/product/{id}", 100).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void addProduct() throws Exception {
        // authService.deleteAllById(1L);
        // Создаем тестовый товар
        // или используйте мок
        //registerUser();
       logoutUser();
        AddProductRequest request = new AddProductRequest();
        request.setProductId(1L);

        mockMvc.perform(post("/api/addProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(authUser())))// Добавляем токен
                .andExpect(status().isOk())
                .andExpect(cookie().exists("user_product_1"));
    }

    @Test
    void checkAllProductsWithCart() throws Exception {
        logoutUser();
        int productsCount = productRepo.findAll().size();
        User user = userRepo.findByEmail(userEmail);
        long cartCount = bucketRepository.findAllByUser(user).stream()
                .flatMapToLong(a -> LongStream.of(a.getAmount()))
                .sum();

        mockMvc
                .perform(get("/api/catalog").contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authUser())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryCount").value(productsCount))
                .andExpect(jsonPath("$.cartCount").value(cartCount));

    }

    @Test
    void failedTransaction() throws Exception {
        mockMvc
                .perform(post("/api/cart/transaction").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }


    @Test
    void checkAllProductsWithEmptyCartAndNoAuth() throws Exception {
        int productsCount = productRepo.findAll().size();

        mockMvc
                .perform(get("/api/catalog").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCount").value(productsCount))
                .andExpect(jsonPath("$.cartCount").value(0));
    }

    @Test
    void checkGetProduct() throws Exception {
        logoutUser();
        BigDecimal productPrice = BigDecimal.valueOf(productRepo.findById(1L).get().getPrice());
        String productTitle = productRepo.findById(1L).get().getName();

        mockMvc.perform(get("/api/catalog/product/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authUser())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(productPrice))
                .andExpect(jsonPath("$.name").value(productTitle));
        logoutUser();

    }

    @Test
    void checkNotExistingUser() throws Exception {
        String notExistingEmail = "notExistingEmail";
        AuthRequest request = new AuthRequest();
        request.setUserEmail(notExistingEmail);
        request.setUserPassword("123");

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(request);

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes))
                .andExpect(status().is(404));
    }
}
