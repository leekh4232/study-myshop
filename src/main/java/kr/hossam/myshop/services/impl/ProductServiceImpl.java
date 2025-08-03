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
        // 상품 목록을 조회하기 위해 ProductMapper의 getProducts 메서드를 호출
        List<Product> products = productMapper.getProducts(input);

        // 조회된 상품 목록 반환
        return products;
    }
}