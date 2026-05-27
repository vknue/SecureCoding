package hr.algebra.glowlog.controller.rest;

import org.springframework.web.bind.annotation.*;
import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.SkinType;
import hr.algebra.glowlog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Products", description = "Skincare product CRUD and search")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single product by ID")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.findById(id));
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name, brand, ingredients, category, skin type, or status")
    public ResponseEntity<List<ProductDto>> search(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) ProductCategory category,
        @RequestParam(required = false) SkinType skinType,
        @RequestParam(required = false) ProductStatus status
    ) {
        return ResponseEntity.ok(productService.search(query, category, skinType, status));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new product (admin only)")
    public ResponseEntity<ProductDto> create(
        @Valid @RequestBody ProductDto dto,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a product (admin only)")
    public ResponseEntity<ProductDto> update(
        @PathVariable Long id,
        @Valid @RequestBody ProductDto dto
    ) {
        try {
            return ResponseEntity.ok(productService.update(id, dto));
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "(admin only)")
    @PostMapping("/adminEndpoint")
    public ResponseEntity<Void> adminEndpoint(
    ) {
        return ResponseEntity.ok().build();
    }



}
