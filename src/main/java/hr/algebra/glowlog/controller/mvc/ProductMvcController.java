package hr.algebra.glowlog.controller.mvc;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            model.addAttribute("product", productService.findById(id));
            return "products/detail";
        } catch (NoSuchElementException e) {
            return "redirect:/products";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute("product", new ProductDto(
            null, "", "", null, null, null, null, null,
            "", null, null, "12M",
            null, null, null, null,
            null, null, null, null, null, null,
            false, false, false, false,
            "", "", "", "",
            null, null, null
        ));
        addEnumsToModel(model);
        model.addAttribute("editMode", false);
        return "products/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
        @Valid @ModelAttribute("product") ProductDto dto,
        BindingResult result,
        @AuthenticationPrincipal User currentUser,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute("editMode", false);
            return "products/form";
        }
        productService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Product added to your shelf.");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("product", productService.findById(id));
            addEnumsToModel(model);
            model.addAttribute("editMode", true);
            return "products/form";
        } catch (NoSuchElementException e) {
            return "redirect:/products";
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
            model.addAttribute("editMode", true);
            return "products/form";
        }
        productService.update(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated.");
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product removed.");
        return "redirect:/products";
    }

    private void addEnumsToModel(Model model) {
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("skinTypes", SkinType.values());
        model.addAttribute("statuses", ProductStatus.values());
        model.addAttribute("routineSlots", RoutineSlot.values());
        model.addAttribute("concerns", SkinConcern.values());
    }
}
