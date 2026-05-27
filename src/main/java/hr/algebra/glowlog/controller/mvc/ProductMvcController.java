package hr.algebra.glowlog.controller.mvc;

import org.springframework.web.bind.annotation.*;

import hr.algebra.glowlog.constants.Constants;
import hr.algebra.glowlog.dto.ProductDto;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.ProductCategory;
import hr.algebra.glowlog.enums.ProductStatus;
import hr.algebra.glowlog.enums.RoutineSlot;
import hr.algebra.glowlog.enums.SkinConcern;
import hr.algebra.glowlog.enums.SkinType;
import hr.algebra.glowlog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/products")
public class ProductMvcController {

    private final ProductService productService;

    public ProductMvcController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) ProductCategory category,
        @RequestParam(required = false) SkinType skinType,
        @RequestParam(required = false) ProductStatus status,
        Model model
    ) {
        boolean searching = query != null || category != null || skinType != null || status != null;
        model.addAttribute("products", searching
            ? productService.search(query, category, skinType, status)
            : productService.findAll());
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("skinTypes", SkinType.values());
        model.addAttribute("statuses", ProductStatus.values());
        model.addAttribute("routineSlots", RoutineSlot.values());
        model.addAttribute("concerns", SkinConcern.values());
        model.addAttribute("query", query);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedSkinType", skinType);
        model.addAttribute("selectedStatus", status);
        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute(Constants.PRODUCT, productService.findById(id));
            return "products/detail";
        } catch (NoSuchElementException e) {
            return Constants.REDIRECT_TO_PRODUCTS;
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute(Constants.PRODUCT, new ProductDto(
            1L, "", "", ProductCategory.ESSENCE, RoutineSlot.AS_NEEDED, SkinType.ALL_TYPES, ProductStatus.ACTIVE, SkinConcern.ACNE,
            "", 50, new BigDecimal(2), "12M",
            LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(),
            3, 3, 3, 3, 3, 3,
            false, false, false, false,
            "", "", "", "",
            "", LocalDateTime.now(), LocalDateTime.now()
        ));
        addEnumsToModel(model);
        model.addAttribute(Constants.EDIT_MODE, false);
        return Constants.PRODUCTS_FORM_ENDPOINT;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
        @Valid @ModelAttribute(Constants.PRODUCT) ProductDto dto,
        BindingResult result,
        @AuthenticationPrincipal User currentUser,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute(Constants.EDIT_MODE, false);
            return Constants.PRODUCTS_FORM_ENDPOINT;
        }
        productService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, "Product added to your shelf.");
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("product", productService.findById(id));
            addEnumsToModel(model);
            model.addAttribute(Constants.EDIT_MODE, true);
            return Constants.PRODUCTS_FORM_ENDPOINT;
        } catch (NoSuchElementException _){
            return Constants.REDIRECT_TO_PRODUCTS;
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("product") ProductDto dto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute(Constants.EDIT_MODE, true);
            return Constants.PRODUCTS_FORM_ENDPOINT;
        }
        productService.update(id, dto);
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, "Product updated.");
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, "Product removed.");
        return Constants.REDIRECT_TO_PRODUCTS;
    }

    private void addEnumsToModel(Model model) {
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("skinTypes", SkinType.values());
        model.addAttribute("statuses", ProductStatus.values());
        model.addAttribute("routineSlots", RoutineSlot.values());
        model.addAttribute("concerns", SkinConcern.values());
    }


}
