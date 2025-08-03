package kr.hossam.myshop.services.impl;

import kr.hossam.myshop.exceptions.ServiceNoResultException;
import kr.hossam.myshop.mappers.CategoryMapper;
import kr.hossam.myshop.models.Category;
import kr.hossam.myshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // <-- 생성자 주입을 위한 어노테이션
public class CategoryServiceImpl implements CategoryService {

    /** 카테고리 관련 SQL을 구현하고 있는 Mapper */
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() throws Exception {
        Category input = new Category();
        input.setParentId(0); // 1depth 카테고리 조회를 위해

        List<Category> output = categoryMapper.getAllCategories(input);

        if (output == null || output.isEmpty()) {
            throw new ServiceNoResultException("조회된 Category 데이터가 없습니다.");
        }

        // 조회된 1depth 카테고리 목록을 순회하며
        for (Category c : output) {
            // 각 카테고리에 속한 하위 카테고리들을 조회
            Category childInput = new Category();
            childInput.setParentId(c.getId());

            List<Category> childCategories = categoryMapper.getAllCategories(childInput);
            c.setSubCategories(childCategories);
        }

        return output;
    }
}