package hr.algebra.glowlog.repository;

import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.SkinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        WHERE (:query IS NULL OR LOWER(p.name)            LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(p.brand)           LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(p.keyIngredients)  LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:category IS NULL OR p.category = :category)
          AND (:skinType IS NULL OR p.skinTypeTarget = :skinType)
          AND (:status IS NULL OR p.status = :status)
        ORDER BY p.holyGrail DESC, p.status ASC, p.brand ASC, p.name ASC
        """)
    List<Product> search(
        @Param("query") String query,
        @Param("category") ProductCategory category,
        @Param("skinType") SkinType skinType,
        @Param("status") ProductStatus status
    );

    List<Product> findAllByOrderByHolyGrailDescStatusAscBrandAscNameAsc();
}
