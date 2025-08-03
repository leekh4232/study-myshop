/**
 * /src/main/java/kr/hossam/myshop/controllers/ProductController.java
 */
package kr.hossam.myshop.controllers;

import kr.hossam.myshop.models.Category;
import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.services.CategoryService;
import kr.hossam.myshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/products")
    public String products(Model model) throws Exception {

        List<Category> categories = categoryService.getAllCategories();
        List<Product> products = productService.getProducts(null);

        // View에 데이터 전달
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        // 사용할 View의 경로 반환
        return "products/index"; // templates/products/index.html
    }
}