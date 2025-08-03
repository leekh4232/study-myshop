package kr.hossam.myshop;

import kr.hossam.myshop.models.Category;
import kr.hossam.myshop.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class CategoryServiceTests {
    @Autowired
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        List<Category> categories = categoryService.getAllCategories();

        for (Category category : categories) {
            log.info("Category ID: {}, Name: {}", category.getId(), category.getName());

            // 하위 카테고리가 있는 경우 출력
            if (category.getSubCategories() != null) {
                for (Category subCategory : category.getSubCategories()) {
                    log.info("  --> SubCategory ID: {}, Name: {}", subCategory.getId(), subCategory.getName());
                }
            }
        }
    }
}