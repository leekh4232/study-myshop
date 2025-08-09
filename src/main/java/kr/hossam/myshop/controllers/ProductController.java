/**
 * /src/main/java/kr/hossam/myshop/controllers/ProductController.java
 */
package kr.hossam.myshop.controllers;

import kr.hossam.myshop.helpers.PageHelper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping({ "/", "/products", "/products/{categoryId}" })
    public String products(
            Model model,
            @PathVariable(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "keyword", required = false) String keyword,
            // 페이지 구현에서 사용할 현재 페이지 번호
            @RequestParam(value = "page", defaultValue = "1") int nowPage) throws Exception {

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

        // 검색어가 전달되었다면 검색어를 상품이름에 대한 검색어로 설정
        if (keyword != null && !keyword.trim().isEmpty()) {
            input.setName(keyword.trim());
        }

        // 페이지 구현을 위한 변수 설정
        int listCount = 20; // 한 페이지당 표시할 목록 수
        int groupCount = 5; // 한 그룹당 표시할 페이지 번호 수

        // 상품 목록 조회 전 전체 데이터 수 조회
        int totalCount = productService.getProductCount(input);

        // 페이지 번호 계산 --> 계산결과를 로그로 출력될 것이다.
        PageHelper pageHelper = new PageHelper(nowPage, totalCount, listCount, groupCount);

        // SQL의 LIMIT절에서 사용될 값을 Beans의 static 변수에 저장
        Product.setOffset(pageHelper.getOffset());
        Product.setListCount(pageHelper.getListCount());

        List<Product> products = productService.getProducts(input);

        // View에 데이터 전달
        model.addAttribute("categoryId", categoryId);       // 사용자가 선택한 카테고리 ID
        model.addAttribute("categoryName", categoryName);   // 사용자가 선택한 카테고리 이름
        model.addAttribute("categories", categories);       // 카테고리 전체 목록 (사이드바 메뉴 구성용)
        model.addAttribute("products", products);           // 상품 목록
        model.addAttribute("keyword", keyword);             // 검색어 (검색 기능 구현을 위해)
        model.addAttribute("pageHelper", pageHelper);       // 페이지 구현을 위한 PageHelper 객체

        // 사용할 View의 경로 반환
        return "products/index"; // templates/products/index.html
    }

    @GetMapping({"/products/detail/{id}", "/products/detail/{id}/{categoryId}"})
    public String productDetail(
            Model model,
            @PathVariable(value="id", required = true) int id,
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

        // 상품 상세 정보 조회
        Product input = new Product();
        input.setId(id);
        Product product = productService.getProductDetail(input);

        // 상품이 존재하지 않으면 404 에러
        if (product == null) {
            throw new NoHandlerFoundException("GET", "/products/detail/" + id + "/" + categoryId, null);
        }

        // View에 데이터 전달
        model.addAttribute("categoryId", categoryId);       // 사용자가 선택한 카테고리 ID
        model.addAttribute("categoryName", categoryName);   // 사용자가 선택한 카테고리 이름
        model.addAttribute("categories", categories);       // 카테고리 전체 목록 (사이드바 메뉴 구성용)
        model.addAttribute("productId", id);                // 상품 ID
        model.addAttribute("product", product);             // 상품 상세 정보

        // 사용할 View의 경로 반환
        return "products/detail"; // templates/products/detail.html
    }
}