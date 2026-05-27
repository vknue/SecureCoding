package hr.algebra.glowlog.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.RoutineSlot;
import hr.algebra.glowlog.enums.SkinConcern;
import hr.algebra.glowlog.enums.SkinType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 250)
    @Column(nullable = false, length = 250)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String brand;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoutineSlot routineSlot;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkinType skinTypeTarget;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    private SkinConcern primaryConcern;

    @Size(max = 500)
    @Column(length = 500)
    private String keyIngredients;

    @Min(1) @Max(2000)
    private Integer volumeMl;

    @DecimalMin("0.0") @DecimalMax("999.99")
    @Column(precision = 6, scale = 2)
    private BigDecimal priceEur;

    @Size(max = 10)
    private String pao;

    private LocalDate purchaseDate;
    private LocalDate openedDate;
    private LocalDate expirationDate;
    private LocalDate finishedDate;

    @Min(1) @Max(50)
    private Integer emptyCount;

    @Min(1) @Max(5)
    private Integer rating;

    @Min(1) @Max(10)
    private Integer effectivenessScore;

    @Min(1) @Max(10)
    private Integer textureScore;

    @Min(1) @Max(10)
    private Integer scentScore;

    @Min(1) @Max(10)
    private Integer valueScore;

    @Column(nullable = false)
    private boolean wouldRepurchase = false;

    @Column(nullable = false)
    private boolean crueltyFree = false;

    @Column(nullable = false)
    private boolean fragranceFree = false;

    @Column(nullable = false)
    private boolean holyGrail = false;

    @Size(max = 200)
    private String moodTags;

    @Size(max = 500)
    @Column(length = 500)
    private String reactionNotes;

    @Size(max = 2000)
    @Column(length = 2000)
    private String review;

    @Size(max = 2000)
    @Column(length = 2000)
    private String personalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_id")
    private User addedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (purchaseDate == null) purchaseDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId()                                { return id; }
    public void setId(Long id)                         { this.id = id; }
    public String getName()                            { return name; }
    public void setName(String name)                   { this.name = name; }
    public String getBrand()                           { return brand; }
    public void setBrand(String brand)                 { this.brand = brand; }
    public ProductCategory getCategory()               { return category; }
    public void setCategory(ProductCategory category)  { this.category = category; }
    public RoutineSlot getRoutineSlot()                { return routineSlot; }
    public void setRoutineSlot(RoutineSlot r)          { this.routineSlot = r; }
    public SkinType getSkinTypeTarget()                { return skinTypeTarget; }
    public void setSkinTypeTarget(SkinType t)          { this.skinTypeTarget = t; }
    public ProductStatus getStatus()                   { return status; }
    public void setStatus(ProductStatus status)        { this.status = status; }
    public SkinConcern getPrimaryConcern()             { return primaryConcern; }
    public void setPrimaryConcern(SkinConcern c)       { this.primaryConcern = c; }
    public String getKeyIngredients()                  { return keyIngredients; }
    public void setKeyIngredients(String k)            { this.keyIngredients = k; }
    public Integer getVolumeMl()                       { return volumeMl; }
    public void setVolumeMl(Integer volumeMl)          { this.volumeMl = volumeMl; }
    public BigDecimal getPriceEur()                    { return priceEur; }
    public void setPriceEur(BigDecimal priceEur)       { this.priceEur = priceEur; }
    public String getPao()                             { return pao; }
    public void setPao(String pao)                     { this.pao = pao; }
    public LocalDate getPurchaseDate()                 { return purchaseDate; }
    public void setPurchaseDate(LocalDate d)           { this.purchaseDate = d; }
    public LocalDate getOpenedDate()                   { return openedDate; }
    public void setOpenedDate(LocalDate d)             { this.openedDate = d; }
    public LocalDate getExpirationDate()               { return expirationDate; }
    public void setExpirationDate(LocalDate d)         { this.expirationDate = d; }
    public LocalDate getFinishedDate()                 { return finishedDate; }
    public void setFinishedDate(LocalDate d)           { this.finishedDate = d; }
    public Integer getEmptyCount()                     { return emptyCount; }
    public void setEmptyCount(Integer emptyCount)      { this.emptyCount = emptyCount; }
    public Integer getRating()                         { return rating; }
    public void setRating(Integer rating)              { this.rating = rating; }
    public Integer getEffectivenessScore()             { return effectivenessScore; }
    public void setEffectivenessScore(Integer e)       { this.effectivenessScore = e; }
    public Integer getTextureScore()                   { return textureScore; }
    public void setTextureScore(Integer t)             { this.textureScore = t; }
    public Integer getScentScore()                     { return scentScore; }
    public void setScentScore(Integer s)               { this.scentScore = s; }
    public Integer getValueScore()                     { return valueScore; }
    public void setValueScore(Integer v)               { this.valueScore = v; }
    public boolean isWouldRepurchase()                 { return wouldRepurchase; }
    public void setWouldRepurchase(boolean w)          { this.wouldRepurchase = w; }
    public boolean isCrueltyFree()                     { return crueltyFree; }
    public void setCrueltyFree(boolean crueltyFree)    { this.crueltyFree = crueltyFree; }
    public boolean isFragranceFree()                   { return fragranceFree; }
    public void setFragranceFree(boolean fragranceFree){ this.fragranceFree = fragranceFree; }
    public boolean isHolyGrail()                       { return holyGrail; }
    public void setHolyGrail(boolean holyGrail)        { this.holyGrail = holyGrail; }
    public String getMoodTags()                        { return moodTags; }
    public void setMoodTags(String moodTags)           { this.moodTags = moodTags; }
    public String getReactionNotes()                   { return reactionNotes; }
    public void setReactionNotes(String r)             { this.reactionNotes = r; }
    public String getReview()                          { return review; }
    public void setReview(String review)               { this.review = review; }
    public String getPersonalNotes()                   { return personalNotes; }
    public void setPersonalNotes(String p)             { this.personalNotes = p; }
    public User getAddedBy()                           { return addedBy; }
    public void setAddedBy(User addedBy)               { this.addedBy = addedBy; }
    public LocalDateTime getCreatedAt()                { return createdAt; }
    public void setCreatedAt(LocalDateTime t)          { this.createdAt = t; }
    public LocalDateTime getUpdatedAt()                { return updatedAt; }
    public void setUpdatedAt(LocalDateTime t)          { this.updatedAt = t; }
}
