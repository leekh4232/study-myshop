/**
 * /src/test/java/kr/hossam/myshop/ProductMapperTests.java
 */
package kr.hossam.myshop;

import kr.hossam.myshop.mappers.ProductMapper;
import kr.hossam.myshop.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class ProductMapperTests {
    @Autowired
    private ProductMapper productMapper;

    @Test
    void testGetProducts() {
        // ProductMapper의 getProducts 메서드를 호출하여 상품 목록을 조회
        List<Product> products = productMapper.getProducts(null);

        // 조회된 상품 목록을 로그로 출력
        for (Product product : products) {
            log.info("Product ID: {}, Name: {}, Price: {}, Discount: {}, Summary: {}, Image URL: {}",
                     product.getId(),
                     product.getName(),
                     product.getPrice(),
                     product.getDiscount(),
                     product.getSummary(),
                     product.getImageUrl());
        }
    }

    @Test
    void testGetProductsByCategory() {
        // ProductMapper의 getProductsByCategory 메서드를 호출하여 특정 카테고리의 상품 목록을 조회
        Product input = new Product();
        input.setCategoryId(10); // 예시로 카테고리 ID 10을 설정

        List<Product> products = productMapper.getProductsByCategory(input);

        // 조회된 상품 목록을 로그로 출력
        for (Product product : products) {
            log.info("Product ID: {}, Name: {}, Price: {}, Discount: {}, Summary: {}, Image URL: {}",
                     product.getId(),
                     product.getName(),
                     product.getPrice(),
                     product.getDiscount(),
                     product.getSummary(),
                     product.getImageUrl());
        }
    }
}