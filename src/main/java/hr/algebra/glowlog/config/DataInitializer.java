package hr.algebra.glowlog.config;

import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.entity.ProductRequest;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.Role;
import hr.algebra.glowlog.enums.RoutineSlot;
import hr.algebra.glowlog.enums.SkinConcern;
import hr.algebra.glowlog.enums.SkinType;
import hr.algebra.glowlog.repository.ProductRepository;
import hr.algebra.glowlog.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository,
        ProductRepository productRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@glowlog.hr");
        admin.setPassword(passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@glowlog.hr");
        user.setPassword(passwordEncoder.encode(System.getenv("USER_PASSWORD")));
        user.setRole(Role.USER);
        userRepository.save(user);

        createProduct(new ProductRequest("Niacinamide 10% + Zinc 1%", "The Ordinary",
            ProductCategory.SERUM, RoutineSlot.AM_AND_PM, SkinType.COMBINATION,
            ProductStatus.ACTIVE, SkinConcern.OIL_CONTROL,
            "Niacinamide 10%, Zinc PCA 1%, Tamarind Gum",
            30, new BigDecimal("7.20"), "12M",
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 3),
            LocalDate.of(2027, 3, 3), null,
            4, 5, 9, 7, 6, 10,
            true, true, false, true,
            "Affordable, effective, oil-balancing daily ritual",
            "No reactions. Skin texture noticeably smoother after 3 weeks.",
            "Cult favorite for a reason. €7 for a holy grail product is wild. Goes under sunscreen perfectly. Repurchased 4 times now.",
            "Pair with hydrating toner to avoid dryness. Don't combine with Vitamin C in the same routine."), admin);

        createProduct(new ProductRequest("Foaming Facial Cleanser", "CeraVe",
            ProductCategory.CLEANSER, RoutineSlot.AM_AND_PM, SkinType.NORMAL,
            ProductStatus.ACTIVE, SkinConcern.BARRIER_REPAIR,
            "Ceramides, Niacinamide, Hyaluronic Acid",
            236, new BigDecimal("13.50"), "12M",
            LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 12),
            LocalDate.of(2027, 2, 12), null,
            3, 4, 8, 8, 5, 9,
            true, false, true, false,
            "Soft foam, gentle, no squeaky feeling",
            "Sometimes a bit drying in winter. Better with creamy cleanser in cold months.",
            "Reliable workhorse cleanser. Recommended by every dermatologist on TikTok. The big pump bottle lasts months.",
            "Switch to hydrating cleanser if barrier feels compromised."), admin);

        createProduct(new ProductRequest("Anthelios UV Mune 400 SPF50+", "La Roche-Posay",
            ProductCategory.SUNSCREEN, RoutineSlot.AM_ONLY, SkinType.ALL_TYPES,
            ProductStatus.ACTIVE, SkinConcern.SUN_PROTECTION,
            "Mexoryl 400, Tinosorb S, Uvinul A Plus",
            50, new BigDecimal("19.90"), "12M",
            LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 6),
            LocalDate.of(2027, 4, 6), null,
            2, 5, 10, 9, 7, 8,
            true, false, false, true,
            "Lightweight, no white cast, glowy finish",
            "Zero breakouts, zero stinging. The gold standard for daily SPF.",
            "Best sunscreen I have ever used. The new Mexoryl 400 filter is genuinely a game-changer. Always in my routine, all year.",
            "Reapply every 2-3 hours when outdoors. Use a 2-finger length for face + neck."), admin);

        createProduct(new ProductRequest("Snail 96 Mucin Power Essence", "COSRX",
            ProductCategory.ESSENCE, RoutineSlot.AM_AND_PM, SkinType.SENSITIVE,
            ProductStatus.ACTIVE, SkinConcern.HYDRATION,
            "Snail Secretion Filtrate 96%, Betaine, Allantoin",
            100, new BigDecimal("21.50"), "12M",
            LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 22),
            LocalDate.of(2027, 1, 22), null,
            2, 5, 9, 7, 8, 9,
            true, true, true, true,
            "Slime-y in the best way, plumping, hydrating",
            "Slight stickiness if you use too much. Two pumps is the sweet spot.",
            "The Korean skincare goat. Mucin gives that glass-skin glow without any irritation. Layers perfectly under serums.",
            "Tap, don't rub. Wait 30 seconds before next step for full absorption."), admin);

        createProduct(new ProductRequest("TLC Sukari Babyfacial", "Drunk Elephant",
            ProductCategory.MASK, RoutineSlot.WEEKLY, SkinType.NORMAL,
            ProductStatus.FINISHED, SkinConcern.TEXTURE,
            "AHA 25%, BHA 2%, Niacinamide, Salicylic Acid",
            50, new BigDecimal("82.00"), "12M",
            LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 5),
            LocalDate.of(2026, 9, 5), LocalDate.of(2026, 3, 15),
            1, 4, 9, 6, 5, 4,
            true, true, false, false,
            "Stings on application, glass skin after",
            "Tingles intensely for the full 20 minutes. Worth it. Sensitive types should patch test.",
            "Pricey but actually effective. The morning after, skin looks airbrushed. Used once a week for 6 months.",
            "Wait at least 24h after using any retinoid. Always SPF the next day. Not for daily use."), admin);

        createProduct(new ProductRequest("Watermelon Glow Niacinamide Dew Drops", "Glow Recipe",
            ProductCategory.SERUM, RoutineSlot.AM_ONLY, SkinType.NORMAL,
            ProductStatus.ABANDONED, SkinConcern.BRIGHTENING,
            "Niacinamide 4%, Watermelon Extract, Hyaluronic Acid",
            40, new BigDecimal("38.00"), "12M",
            LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 12),
            LocalDate.of(2026, 6, 12), null,
            1, 2, 4, 7, 8, 3,
            false, true, false, false,
            "Pretty pink packaging, smells like candy",
            "Caused tiny bumps after 2 weeks. Stopped using. Possibly fragrance reaction.",
            "All packaging, mid results. The fragrance is intense for sensitive types. The Ordinary niacinamide does the same job for one fifth of the price.",
            "Gifted from a friend. Wouldn't buy myself."), admin);

        createProduct(new ProductRequest("Glow Serum Propolis + Niacinamide", "Beauty of Joseon",
            ProductCategory.SERUM, RoutineSlot.AM_AND_PM, SkinType.SENSITIVE,
            ProductStatus.REPURCHASE, SkinConcern.BRIGHTENING,
            "Propolis Extract 60%, Niacinamide 2%",
            30, new BigDecimal("16.50"), "12M",
            LocalDate.of(2026, 3, 25), LocalDate.of(2026, 3, 27),
            LocalDate.of(2027, 3, 27), null,
            3, 5, 9, 9, 8, 10,
            true, true, false, true,
            "Honey-glow finish, calming, dewy",
            "Zero irritation, zero breakouts, only good things.",
            "Cannot live without this. The propolis calms redness within days. My 3rd bottle and counting. K-beauty supremacy.",
            "Best applied to slightly damp skin. Layers well with everything."), admin);

        createProduct(new ProductRequest("2% BHA Liquid Exfoliant", "Paula's Choice",
            ProductCategory.EXFOLIANT, RoutineSlot.PM_ONLY, SkinType.ACNE_PRONE,
            ProductStatus.ACTIVE, SkinConcern.ACNE,
            "Salicylic Acid 2%, Green Tea Extract, Methylpropanediol",
            118, new BigDecimal("36.00"), "12M",
            LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 4),
            LocalDate.of(2027, 2, 4), null,
            2, 5, 10, 8, 6, 9,
            true, true, true, true,
            "Watery texture, unclogs pores, evens texture",
            "Slight tingle on first use. No purging period for me. Just clear skin.",
            "Has fixed my texture single-handedly. Use every other PM. The bottle lasts almost a year. Worth every euro.",
            "Start 2x a week, build up tolerance. Don't combine with retinoid same night."), admin);

        createProduct(new ProductRequest("Good Genes Lactic Acid Treatment", "Sunday Riley",
            ProductCategory.EXFOLIANT, RoutineSlot.PM_ONLY, SkinType.NORMAL,
            ProductStatus.WANT_TO_TRY, SkinConcern.HYPERPIGMENTATION,
            "Lactic Acid 5%, Licorice, Lemongrass",
            30, new BigDecimal("95.00"), "12M",
            null, null, null, null,
            null, null, null, null, null, null,
            false, true, false, false,
            "Cult favorite, all over BeautyTok",
            null,
            "Saving up. Reviews say it's worth the splurge for stubborn hyperpigmentation. Will compare to Paula's BHA after I try.",
            "Wait for a sale or Sephora birthday gift. Never pay full price."), admin);

        createProduct(new ProductRequest("Lip Sleeping Mask Berry", "Laneige",
            ProductCategory.LIP_CARE, RoutineSlot.PM_ONLY, SkinType.ALL_TYPES,
            ProductStatus.ACTIVE, SkinConcern.HYDRATION,
            "Hyaluronic Acid, Vitamin C, Murumuru Butter",
            20, new BigDecimal("24.00"), "12M",
            LocalDate.of(2026, 3, 8), LocalDate.of(2026, 3, 10),
            LocalDate.of(2027, 3, 10), null,
            2, 5, 9, 10, 10, 7,
            true, false, false, true,
            "Berry-scented bedtime ritual, plump lips by morning",
            "Smells incredible without being overwhelming. Cute pink jar lives on my nightstand.",
            "Best lip product ever. Wake up with perfect lips. Lasts 6+ months easily. Beauty influencer favorite for a reason.",
            "Apply generously before bed. Comes with a tiny spatula in the jar."), admin);
    }

    private void createProduct(ProductRequest product, User addedBy) {
        Product p = new Product();
        p.setName(product.name());
        p.setBrand(product.brand());
        p.setCategory(product.category());
        p.setRoutineSlot(product.routine());
        p.setSkinTypeTarget(product.skinType());
        p.setStatus(product.status());
        p.setPrimaryConcern(product.concern());
        p.setKeyIngredients(product.ingredients());
        p.setVolumeMl(product.volume());
        p.setPriceEur(product.price());
        p.setPao(product.pao());
        p.setPurchaseDate(product.purchased());
        p.setOpenedDate(product.opened());
        p.setExpirationDate(product.expiration());
        p.setFinishedDate(product.finished());
        p.setEmptyCount(product.emptyCount());
        p.setRating(product.rating());
        p.setEffectivenessScore(product.effectiveness());
        p.setTextureScore(product.texture());
        p.setScentScore(product.scent());
        p.setValueScore(product.value());
        p.setWouldRepurchase(product.repurchase());
        p.setCrueltyFree(product.cruelty());
        p.setFragranceFree(product.fragranceFree());
        p.setHolyGrail(product.holyGrail());
        p.setMoodTags(product.mood());
        p.setReactionNotes(product.reactions());
        p.setReview(product.review());
        p.setPersonalNotes(product.notes());
        p.setAddedBy(addedBy);
        productRepository.save(p);
    }
}
