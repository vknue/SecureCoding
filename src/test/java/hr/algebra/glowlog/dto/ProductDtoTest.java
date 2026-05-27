package hr.algebra.glowlog.dto;

import static org.junit.jupiter.api.Assertions.*;

import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.enums.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testMappingFromEntity() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setBrand("Brand");

        ProductDto dto = ProductDto.from(product);

        assertEquals("Test", dto.name());
        assertEquals(1L, dto.id());
    }

    @Test
    void testApplyToEntity() {
        Product product = new Product();
        ProductDto dto = new ProductDto(null, "New Name", "New Brand",
                ProductCategory.CLEANSER, RoutineSlot.AM_ONLY, SkinType.OILY,
                ProductStatus.ACTIVE, SkinConcern.ACNE, "Water", 100, BigDecimal.TEN,
                "12M", null, null, null, null, 1, 5, 5, 5, 5, 5,
                true, true, true, true, "Fresh", "None", "Good", "Private", null, null, null);

        dto.applyTo(product);

        assertEquals("New Name", product.getName());
        assertEquals(ProductCategory.CLEANSER, product.getCategory());
    }

    @Test
    void testValidationConstraints() {
        // Test invalid data (name is blank, volume is 0)
        ProductDto invalidDto = new ProductDto(null, "", "Brand", null, null, null, null, null,
                null, 0, null, null, null, null, null, null, 0, null, null, null, null, null,
                false, false, false, false, null, null, null, null, null, null, null);

        assertFalse(validator.validate(invalidDto).isEmpty());
    }
}