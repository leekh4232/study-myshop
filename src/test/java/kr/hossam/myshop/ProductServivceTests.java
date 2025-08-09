/**
 * /src/test/java/kr/hossam/myshop/ProductServivceTests.java
 */
package kr.hossam.myshop;

import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class ProductServivceTests {

    @Autowired
    private ProductService productService;

    @Test
    void testGetProducts() throws Exception {
        List<Product> productList = productService.getProducts(null);

        for (Product product : productList) {
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
    void testGetProductDetail() throws Exception {
        Product input = new Product();
        input.setId(11023); // 예시로 ID가 11023인 상품의 상세 정보를 조회

        Product productDetail = productService.getProductDetail(input);

        log.info("Product Detail: ID: {}, Name: {}, Price: {}, Discount: {}, Image URL: {}, options: {}",
                 productDetail.getId(),
                 productDetail.getName(),
                 productDetail.getPrice(),
                 productDetail.getDiscount(),
                 productDetail.getImageUrl(),
                 productDetail.getOptions());
    }
}