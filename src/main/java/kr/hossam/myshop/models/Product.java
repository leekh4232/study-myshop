package kr.hossam.myshop.models;

import lombok.Data;

@Data
public class Product {
    private int id;
    private int categoryId;
    private String name;
    private int price;
    private int discount;
    private String summary;
    private String imageUrl;
    private String deliveryInfo;
    private String productUrl;
    private String regDate;
    private String editDate;
}