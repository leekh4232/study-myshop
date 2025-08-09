/**
 * /src/main/java/kr/hossam/myshop/services/impl/ProductServiceImpl.java
 */
package kr.hossam.myshop.services.impl;

import kr.hossam.myshop.mappers.ProductMapper;
import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public List<Product> getProducts(Product input) throws Exception {
        List<Product> products = null;

        if (input != null && input.getCategoryId() > 0) {
            // 카테고리 ID가 설정되어 있다면 해당 카테고리에 속한 상품 목록 조회
            products = productMapper.getProductsByCategory(input);
        } else {
            // 카테고리 ID가 설정되어 있지 않다면 전체 상품 목록 조회
            products = productMapper.getProducts(input);
        }

        // 조회된 상품 목록 반환
        return products;
    }

    @Override
    public int getProductCount(Product input) throws Exception {
        int count = 0;

        if (input != null && input.getCategoryId() > 0) {
            // 카테고리 ID가 설정되어 있다면 해당 카테고리에 속한 상품 개수 조회
            count = productMapper.getProductCount(input);
        } else {
            // 카테고리 ID가 설정되어 있지 않다면 전체 상품 개수 조회
            count = productMapper.getProductCount(input);
        }

        // 조회된 상품 개수 반환
        return count;
    }
}