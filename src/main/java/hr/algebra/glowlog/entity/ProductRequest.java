package hr.algebra.glowlog.entity;

import hr.algebra.glowlog.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductRequest(
        String name,
        String brand,
        ProductCategory category,
        RoutineSlot routine,
        SkinType skinType,
        ProductStatus status,
        SkinConcern concern,
        String ingredients,
        Integer volume,
        BigDecimal price,
        String pao,
        LocalDate purchased,
        LocalDate opened,
        LocalDate expiration,
        LocalDate finished,
        Integer emptyCount,
        Integer rating,
        Integer effectiveness,
        Integer texture,
        Integer scent,
        Integer value,
        boolean repurchase,
        boolean cruelty,
        boolean fragranceFree,
        boolean holyGrail,
        String mood,
        String reactions,
        String review,
        String notes
) {}