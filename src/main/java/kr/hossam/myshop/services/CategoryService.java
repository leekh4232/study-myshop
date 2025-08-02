package kr.hossam.myshop.services;

import kr.hossam.myshop.models.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 모든 카테고리를 조회한다.
     *
     * @return  List<Category> - 카테고리 목록
     */
    public List<Category> getAllCategories();
}