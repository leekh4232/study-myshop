package kr.hossam.myshop;

import kr.hossam.myshop.mappers.CategoryMapper;
import kr.hossam.myshop.models.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class CategoryMapperTests {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void testGet1DepthCategories() {
        Category input = new Category();
        input.setParentId(0); // 1depth 카테고리 조회를 위해

        List<Category> categoryList = categoryMapper.getAllCategories(input);

        for (Category category : categoryList) {
            log.info("Category ID: {}, Name: {}, RegDate: {}, EditDate: {}",
                     category.getId(),
                     category.getName(),
                     category.getRegDate(),
                     category.getEditDate());
        }
    }

    @Test
    void testGet2DepthCategories() {
        Category input = new Category();
        input.setParentId(7); // 1depth 카테고리의 ID값을 설정

        List<Category> categoryList = categoryMapper.getAllCategories(input);

        for (Category category : categoryList) {
            log.info("Category ID: {}, Name: {}, RegDate: {}, EditDate: {}",
                     category.getId(),
                     category.getName(),
                     category.getRegDate(),
                     category.getEditDate());
        }
    }
}