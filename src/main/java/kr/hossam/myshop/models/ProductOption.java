package kr.hossam.myshop.models;

import lombok.Data;

@Data
public class ProductOption {
    private int id;
    private int productId;
    private String name;
    private String value;
    private String regDate; // 등록일
    private String editDate; // 수정일
}