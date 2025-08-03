package kr.hossam.myshop.models;

import lombok.Data;

import java.util.List;

@Data
public class Category {
    private int id;
    private String name;
    private int parentId;
    private int sort;
    private String regDate;
    private String editDate;

    // 하위 카테고리 목록 -> null인 경우 하위 카테고리 없음
    private List<Category> subCategories;
}