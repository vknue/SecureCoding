package hr.algebra.glowlog.dto;

import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.RoutineSlot;
import hr.algebra.glowlog.enums.SkinConcern;
import hr.algebra.glowlog.enums.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Skincare product data transfer object")
public record ProductDto(

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @NotBlank @Size(max = 250)
    @Schema(description = "Product name", example = "Niacinamide 10% + Zinc 1%")
    String name,

    @NotBlank @Size(max = 100)
    @Schema(description = "Brand", example = "The Ordinary")
    String brand,

    @NotNull
    @Schema(description = "Product category")
    ProductCategory category,

    @NotNull
    @Schema(description = "When to use it")
    RoutineSlot routineSlot,

    @NotNull
    @Schema(description = "Target skin type")
    SkinType skinTypeTarget,

    @NotNull
    @Schema(description = "Current status")
    ProductStatus status,

    @Schema(description = "Primary skin concern this product targets")
    SkinConcern primaryConcern,

    @Size(max = 500)
    @Schema(description = "Key active ingredients", example = "Niacinamide, Zinc PCA, Hyaluronic Acid")
    String keyIngredients,

    @Min(1) @Max(2000)
    @Schema(description = "Volume in millilitres", example = "30")
    Integer volumeMl,

    @DecimalMin("0.0") @DecimalMax("999.99")
    @Schema(description = "Price in EUR", example = "7.20")
    BigDecimal priceEur,

    @Size(max = 10)
    @Schema(description = "Period After Opening", example = "12M")
    String pao,

    @Schema(description = "Date purchased")
    LocalDate purchaseDate,

    @Schema(description = "Date opened")
    LocalDate openedDate,

    @Schema(description = "Expiration date")
    LocalDate expirationDate,

    @Schema(description = "Date finished")
    LocalDate finishedDate,

    @Min(1) @Max(50)
    @Schema(description = "How many bottles you've gone through (lifetime)", example = "3")
    Integer emptyCount,

    @Min(1) @Max(5)
    @Schema(description = "Overall rating 1-5 stars", example = "5")
    Integer rating,

    @Min(1) @Max(10)
    @Schema(description = "Effectiveness score", example = "9")
    Integer effectivenessScore,

    @Min(1) @Max(10)
    @Schema(description = "Texture / feel score", example = "8")
    Integer textureScore,

    @Min(1) @Max(10)
    @Schema(description = "Scent score", example = "7")
    Integer scentScore,

    @Min(1) @Max(10)
    @Schema(description = "Value for money score", example = "10")
    Integer valueScore,

    @Schema(description = "Would you repurchase")
    boolean wouldRepurchase,

    @Schema(description = "Cruelty-free brand")
    boolean crueltyFree,

    @Schema(description = "Fragrance-free formula")
    boolean fragranceFree,

    @Schema(description = "Holy grail status")
    boolean holyGrail,

    @Size(max = 200)
    @Schema(description = "Mood / vibe tags", example = "Cooling, dewy, slime-y but in a good way")
    String moodTags,

    @Size(max = 500)
    @Schema(description = "Skin reactions or notes")
    String reactionNotes,

    @Size(max = 2000)
    @Schema(description = "Personal review")
    String review,

    @Size(max = 2000)
    @Schema(description = "Private notes")
    String personalNotes,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String addedBy,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime createdAt,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime updatedAt
) {
    public static ProductDto from(Product p) {
        return new ProductDto(
            p.getId(),
            p.getName(),
            p.getBrand(),
            p.getCategory(),
            p.getRoutineSlot(),
            p.getSkinTypeTarget(),
            p.getStatus(),
            p.getPrimaryConcern(),
            p.getKeyIngredients(),
            p.getVolumeMl(),
            p.getPriceEur(),
            p.getPao(),
            p.getPurchaseDate(),
            p.getOpenedDate(),
            p.getExpirationDate(),
            p.getFinishedDate(),
            p.getEmptyCount(),
            p.getRating(),
            p.getEffectivenessScore(),
            p.getTextureScore(),
            p.getScentScore(),
            p.getValueScore(),
            p.isWouldRepurchase(),
            p.isCrueltyFree(),
            p.isFragranceFree(),
            p.isHolyGrail(),
            p.getMoodTags(),
            p.getReactionNotes(),
            p.getReview(),
            p.getPersonalNotes(),
            p.getAddedBy() != null ? p.getAddedBy().getUsername() : null,
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }

    public void applyTo(Product p) {
        p.setName(name);
        p.setBrand(brand);
        p.setCategory(category);
        p.setRoutineSlot(routineSlot);
        p.setSkinTypeTarget(skinTypeTarget);
        p.setStatus(status);
        p.setPrimaryConcern(primaryConcern);
        p.setKeyIngredients(keyIngredients);
        p.setVolumeMl(volumeMl);
        p.setPriceEur(priceEur);
        p.setPao(pao);
        p.setPurchaseDate(purchaseDate);
        p.setOpenedDate(openedDate);
        p.setExpirationDate(expirationDate);
        p.setFinishedDate(finishedDate);
        p.setEmptyCount(emptyCount);
        p.setRating(rating);
        p.setEffectivenessScore(effectivenessScore);
        p.setTextureScore(textureScore);
        p.setScentScore(scentScore);
        p.setValueScore(valueScore);
        p.setWouldRepurchase(wouldRepurchase);
        p.setCrueltyFree(crueltyFree);
        p.setFragranceFree(fragranceFree);
        p.setHolyGrail(holyGrail);
        p.setMoodTags(moodTags);
        p.setReactionNotes(reactionNotes);
        p.setReview(review);
        p.setPersonalNotes(personalNotes);
    }
}
