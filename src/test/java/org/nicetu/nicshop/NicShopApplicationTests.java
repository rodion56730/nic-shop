package org.nicetu.nicshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.domain.Category;
import org.nicetu.nicshop.dto.JwtResponseDto;
import org.nicetu.nicshop.repository.CategoryRepository;
import org.nicetu.nicshop.repository.ItemRepository;
import org.nicetu.nicshop.repository.UserItemRepository;
import org.nicetu.nicshop.repository.UserRepository;
import org.nicetu.nicshop.requests.AddItemRequest;
import org.nicetu.nicshop.requests.AuthRequest;
import org.nicetu.nicshop.requests.admin.AddPropertyRequest;
import org.nicetu.nicshop.requests.admin.CategoryRequest;
import org.nicetu.nicshop.requests.admin.ItemRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.security.jwt.JwtProvider;
import org.nicetu.nicshop.security.jwt.JwtUtils;
import org.nicetu.nicshop.service.api.AuthService;
import org.nicetu.nicshop.service.api.admin.CategoryAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.LongStream;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class NicShopApplicationTests {
    private final String userEmail = "test@gmail.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemRepository productRepo;
    @Autowired
    private UserItemRepository bucketRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CategoryRepository categoryRepository;


    @InjectMocks
    private CategoryAdminService adminService;

    private CategoryRequest categoryRequest;

    @Autowired
    public NicShopApplicationTests(
            MockMvc mockMvc,
            ItemRepository productRepo,
            UserItemRepository bucketRepository,
            AuthService authService,
            JwtProvider jwtProvider,
            UserRepository userRepo,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.productRepo = productRepo;
        this.bucketRepository = bucketRepository;
        this.authService = authService;
        this.jwtProvider = jwtProvider;
        this.userRepo = userRepo;
        this.objectMapper = objectMapper;
    }
    
    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest();
        categoryRequest.setId(1L);
        categoryRequest.setName("Electronics");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setName("Smartphone");
        itemRequest.setSubcategoryId(1L);

        AddPropertyRequest addPropertyRequest = new AddPropertyRequest();
        addPropertyRequest.setId(1L);
        addPropertyRequest.setName("Color");
        addPropertyRequest.setValue("Black");
    }

    public JwtAuthentication authUser(String userEmail) {
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
        mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void notExistingProduct() throws Exception {
        mockMvc.perform(get("/api/catalog/product/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addProduct() throws Exception {
        logoutUser();
        AddItemRequest request = new AddItemRequest();
        request.setProductId(1L);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(authUser(userEmail))))
                .andExpect(status().isOk());
    }

    @Test
    void checkAllProductsWithCart() throws Exception {
        int productsCount = productRepo.findAll().size();
        User user = userRepo.findByEmail(userEmail);
        long cartCount = bucketRepository.findAllByUser(user).stream()
                .flatMapToLong(a -> LongStream.of(a.getAmount()))
                .sum();

        mockMvc.perform(get("/api/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authUser(userEmail))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryCount").value(productsCount))
                .andExpect(jsonPath("$.cartCount").value(cartCount));
    }

    @Test
    void failedTransaction() throws Exception {
        mockMvc.perform(post("/api/cart/transaction")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkAllProductsWithEmptyCartAndNoAuth() throws Exception {
        int productsCount = productRepo.findAll().size();

        mockMvc.perform(get("/api/catalog")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCount").value(productsCount))
                .andExpect(jsonPath("$.cartCount").value(0));
    }

    @Test
    void checkGetProduct() throws Exception {
        logoutUser();
        BigDecimal productPrice = BigDecimal.valueOf(productRepo.findById(1L).get().getPrice());
        String productTitle = productRepo.findById(1L).isPresent()? productRepo.findById(1L).get().getName() : "";

        mockMvc.perform(get("/api/catalog/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authUser(userEmail))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
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

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bytes))
                .andExpect(status().isNotFound());
    }

    @Test
    void addCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());

        Category result = adminService.addCategory(categoryRequest);

        assertNotNull(result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_NotFound_ThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> adminService.updateCategory(categoryRequest)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Данной категории не существует", exception.getReason());
    }
}
