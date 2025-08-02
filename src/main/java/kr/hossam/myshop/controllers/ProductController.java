package kr.hossam.myshop.controllers;

import kr.hossam.myshop.models.Category;
import kr.hossam.myshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model) {

        List<Category> categories = categoryService.getAllCategories();

        // View에 데이터 전달
        model.addAttribute("categories", categories);

        // 사용할 View의 경로 반환
        return "products/index"; // templates/products/index.html
    }
}