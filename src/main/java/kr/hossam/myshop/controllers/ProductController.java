/**
 * /src/main/java/kr/hossam/myshop/controllers/ProductController.java
 */
package kr.hossam.myshop.controllers;

import kr.hossam.myshop.models.Category;
import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.services.CategoryService;
import kr.hossam.myshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping({"/", "/products", "/products/{categoryId}"})
    public String products(Model model,
            @PathVariable(value="categoryId", required = false) Integer categoryId) throws Exception {

        // 카테고리 목록을 가져옴
        List<Category> categories = categoryService.getAllCategories();

        // Path 파라미터가 전달되었다면 (카테고리가 선택되었다면) 카테고리 이름 찾기
        String categoryName = "ALL PRODUCTS";

        if (categoryId != null && categoryId > 0) {
            for (Category c : categories) {
                if (c.getId() == categoryId) {
                    categoryName = c.getName();
                    break;
                }
            }
        }

        // 상품 목록을 가져옴
        Product input = new Product();

        // Path 파라미터가 전달되지 않으면 설정 안함 --> 전체 상품 목록 조회
        if (categoryId != null && categoryId > 0) {
            input.setCategoryId(categoryId);
        }
        List<Product> products = productService.getProducts(input);

        // View에 데이터 전달
        model.addAttribute("categoryId", categoryId);       // 사용자가 선택한 카테고리 ID
        model.addAttribute("categoryName", categoryName);   // 사용자가 선택한 카테고리 이름
        model.addAttribute("categories", categories);       // 카테고리 전체 목록 (사이드바 메뉴 구성용)
        model.addAttribute("products", products);           // 상품 목록

        // 사용할 View의 경로 반환
        return "products/index"; // templates/products/index.html
    }
}