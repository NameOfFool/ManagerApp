package manager_app.controller;

import lombok.RequiredArgsConstructor;
import manager_app.dto.CreateProductDto;
import manager_app.entity.Product;
import manager_app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "catalog/products")
public class ProductsController {

    private final ProductService productService;

    @GetMapping(value = "list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productService.findAllProducts());
        return "catalog/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalog/products/new";
    }

    @PostMapping("create")
    public String createProduct(CreateProductDto payload) {
        Product p = this.productService.createProduct(payload.title(), payload.details());
        return "redirect:/catalog/products/%d".formatted(p.getId());
    }
}
