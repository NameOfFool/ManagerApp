package manager.controller;

import lombok.RequiredArgsConstructor;
import manager.client.ProductsRestClient;
import manager.dto.CreateProductDto;
import manager.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "catalog/products")
public class ProductsController {

    private final ProductsRestClient productsRestClient;

    @GetMapping(value = "list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productsRestClient.findAllProducts());
        return "catalog/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalog/products/new";
    }

    @PostMapping("create")
    public String createProduct(CreateProductDto payload) {
        Product p = this.productsRestClient.createProduct(payload.title(), payload.details());
        return "redirect:/catalog/products/%d".formatted(p.id());
    }
}
