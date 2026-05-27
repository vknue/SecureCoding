package hr.algebra.glowlog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.*;
import hr.algebra.glowlog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @InjectMocks private ProductService productService;

    private ProductDto createDummyDto() {
        return new ProductDto(
                1L, "Test Product", "Test Brand", ProductCategory.CLEANSER, RoutineSlot.AM_ONLY,
                SkinType.OILY, ProductStatus.ACTIVE, SkinConcern.ACNE, "Water", 100,
                BigDecimal.TEN, "12M", null, null, null, null, 1, 5, 5, 5, 5, 5,
                true, true, true, true, "Fresh", "None", "Good", "Private", "admin", null, null
        );
    }

    @Test
    void testFindAll() {
        when(productRepository.findAllByOrderByHolyGrailDescStatusAscBrandAscNameAsc())
                .thenReturn(List.of(new Product()));

        List<ProductDto> result = productService.findAll();

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByIdSuccess() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.findById(1L));
    }

    @Test
    void testCreate() {
        ProductDto dto = createDummyDto();
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        ProductDto result = productService.create(dto, new User());

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateSuccess() {
        Product existingProduct = new Product();
        ProductDto dto = createDummyDto();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        ProductDto result = productService.update(1L, dto);

        assertNotNull(result);
    }

    @Test
    void testDeleteSuccess() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.delete(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> productService.delete(1L));
    }
}