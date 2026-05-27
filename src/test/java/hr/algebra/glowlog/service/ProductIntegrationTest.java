package hr.algebra.glowlog.service;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.enums.*;
import hr.algebra.glowlog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.findAll()).thenReturn(List.of(new ProductDto(
                1L, "Test Product", "Test Brand", ProductCategory.CLEANSER, RoutineSlot.AM_ONLY,
                SkinType.OILY, ProductStatus.ACTIVE, SkinConcern.ACNE, "Water", 100,
                BigDecimal.TEN, "12M", null, null, null, null, 1, 5, 5, 5, 5, 5,
                true, true, true, true, "Fresh", "None", "Good", "Private", "admin", null, null
        )));

        mockMvc.perform(get("/api/products").with(user("admin").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByIdFound() throws Exception {
        when(productService.findById(1L)).thenReturn(new ProductDto(
                1L, "Test Product", "Test Brand", ProductCategory.CLEANSER, RoutineSlot.AM_ONLY,
                SkinType.OILY, ProductStatus.ACTIVE, SkinConcern.ACNE, "Water", 100,
                BigDecimal.TEN, "12M", null, null, null, null, 1, 5, 5, 5, 5, 5,
                true, true, true, true, "Fresh", "None", "Good", "Private", "admin", null, null
        ));

        mockMvc.perform(get("/api/products/1").with(user("admin").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(productService.findById(99L)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/api/products/99").with(user("admin").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProductAsAdmin() throws Exception {
        ProductDto dto = new ProductDto(
                1L, "Test Product", "Test Brand", ProductCategory.CLEANSER, RoutineSlot.AM_ONLY,
                SkinType.OILY, ProductStatus.ACTIVE, SkinConcern.ACNE, "Water", 100,
                BigDecimal.TEN, "12M", null, null, null, null, 1, 5, 5, 5, 5, 5,
                true, true, true, true, "Fresh", "None", "Good", "Private", "admin", null, null
        );
        when(productService.create(any(), any())).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateProductAsUserForbidden() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .with(user("admin").roles("USER")))
                .andExpect(status().isForbidden());
    }
}