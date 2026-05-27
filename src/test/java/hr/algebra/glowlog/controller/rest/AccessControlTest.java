package hr.algebra.glowlog.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.glowlog.config.SecurityConfig;
import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.RoutineSlot;
import hr.algebra.glowlog.enums.SkinType;
import hr.algebra.glowlog.security.JwtService;
import hr.algebra.glowlog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.ProtectionDomain;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
@Import(SecurityConfig.class)
public class AccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "USER")
    void testAdminAccessDeniedForUser() throws Exception {
        ProductDto dummyProduct = new ProductDto(
                null, "Test Product", "Test Brand",
                ProductCategory.SERUM, RoutineSlot.AM_ONLY, SkinType.DRY,
                ProductStatus.ACTIVE, null, "Ingredients", 30,
                new BigDecimal("10.00"), "12M", null, null, null, null,
                1, 5, 5, 5, 5, 5, true, true, true, true,
                "Cooling", "None", "Great", "Private", null, null, null
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyProduct)))
                .andExpect(status().isUnauthorized());
    }
}