/**
 * /src/test/java/kr/hossam/myshop/ProductMapperTests.java
 */
package kr.hossam.myshop;

import kr.hossam.myshop.mappers.ProductMapper;
import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.models.ProductOption;
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

    @Test
    void testGetProductCount() {
        // 전체 상품 개수를 조회하기 위해 ProductMapper의 getProductCount 메서드를 호출
        // 입력 파라미터가 null인 경우 전체 상품 개수를 조회
        int productCount = productMapper.getProductCount(null);

        // 조회된 상품 개수를 로그로 출력
        log.info("Total Products Count: {}", productCount);
    }

    @Test
    void testGetProductCountByCategory() {
        // 특정 카테고리의 상품 개수를 조회하기 위해 ProductMapper의 getProductCountByCategory 메서드를 호출
        Product input = new Product();
        input.setCategoryId(10); // 예시로 카테고리 ID 10을 설정

        int productCount = productMapper.getProductCountByCategory(input);

        // 조회된 상품 개수를 로그로 출력
        log.info("Total Products Count in Category ID 10: {}", productCount);
    }

    @Test
    void getProductById() {
        // 특정 상품 ID로 상품 정보를 조회하기 위해 ProductMapper의 getProductById 메서드를 호출
        Product input = new Product();
        input.setId(11023); // 예시로 상품 ID 11023 설정 --> 실제 DB에 존재하는 ID를 설정해야 함

        Product product = productMapper.getProductById(input);

        // 조회된 상품 정보를 로그로 출력
        log.info("Product ID: {}, Name: {}, Price: {}, Discount: {}, Summary: {}, Image URL: {}",
                 product.getId(),
                 product.getName(),
                 product.getPrice(),
                 product.getDiscount(),
                 product.getSummary(),
                 product.getImageUrl());
    }

    @Test
    void testGetProductOptionNames() {
        ProductOption input = new ProductOption();
        input.setProductId(11023);

        // ProductMapper의 getProductOptions 메서드를 호출하여 상품 옵션 목록을 조회
        String[] output = productMapper.getProductOptionNames(input);

        for (String optionName : output) {
            log.info("Option Name: {}", optionName);
        }
    }

    @Test
    void testGetProductOptionsByName() {
        ProductOption input = new ProductOption();
        input.setProductId(11023);
        input.setName("사이즈"); // 예시로 "사이즈"라는 옵션 이름을 설정

        // ProductMapper의 getProductOptionsByName 메서드를 호출하여 특정 옵션 이름의 상품 옵션 목록을 조회
        List<ProductOption> options = productMapper.getProductOptionsByName(input);

        for (ProductOption option : options) {
            log.info("Option ID: {}, Name: {}, Value: {}", option.getId(), option.getName(), option.getValue());
        }
    }
}