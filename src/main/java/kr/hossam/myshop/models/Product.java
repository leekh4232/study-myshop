/**
 * /src/main/java/kr/hossam/myshop/models/Product.java
 */
package kr.hossam.myshop.models;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private int price;
    private int discount;
    private String summary;
    private String imageUrl;
    private String deliveryInfo;
    private String mdComment;
    private String productUrl;
    private String content;
    private String regDate;
    private String editDate;

    // 카테고리 테이블에 대한 JOIN을 위한 필드
    private int categoryId;
    private String categoryName;
}