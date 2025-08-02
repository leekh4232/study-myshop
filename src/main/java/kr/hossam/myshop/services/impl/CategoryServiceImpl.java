package kr.hossam.myshop.services.impl;

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
    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategories();
    }
}