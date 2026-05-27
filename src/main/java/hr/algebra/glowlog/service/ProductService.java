package hr.algebra.glowlog.service;

import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.SkinType;
import hr.algebra.glowlog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> findAll() {
        return productRepository.findAllByOrderByHolyGrailDescStatusAscBrandAscNameAsc()
            .stream()
            .map(ProductDto::from)
            .toList();
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id)
            .map(ProductDto::from)
            .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }

    public List<ProductDto> search(String query, ProductCategory category, SkinType skinType, ProductStatus status) {
        String nq = (query != null && query.isBlank()) ? null : query;
        return productRepository.search(nq, category, skinType, status)
            .stream()
            .map(ProductDto::from)
            .toList();
    }



    @Transactional
    public ProductDto create(ProductDto dto, User creator) {
        Product product = new Product();
        dto.applyTo(product);
        product.setAddedBy(creator);
        return ProductDto.from(productRepository.save(product));
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        dto.applyTo(product);
        return ProductDto.from(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

}
