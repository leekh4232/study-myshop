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
    void testGetAllCategories() {
        List<Category> categoryList = categoryMapper.getAllCategories();

        for (Category category : categoryList) {
            log.info("Category ID: {}, Name: {}, RegDate: {}, EditDate: {}",
                     category.getId(),
                     category.getName(),
                     category.getRegDate(),
                     category.getEditDate());
        }
    }
}