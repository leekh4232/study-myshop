/**
 * /src/main/java/kr/hossam/myshop/services/impl/ProductServiceImpl.java
 */
package kr.hossam.myshop.services.impl;

import kr.hossam.myshop.mappers.ProductMapper;
import kr.hossam.myshop.models.Product;
import kr.hossam.myshop.models.ProductOption;
import kr.hossam.myshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            count = productMapper.getProductCountByCategory(input);
        } else {
            // 카테고리 ID가 설정되어 있지 않다면 전체 상품 개수 조회
            count = productMapper.getProductCount(input);
        }

        // 조회된 상품 개수 반환
        return count;
    }

    @Override
    public Product getProductDetail(Product input) throws Exception {
        Product product = productMapper.getProductById(input);

        // 이 상품에 속한 상품옵션 이름을 조회한다.
        ProductOption productOption = new ProductOption();
        productOption.setProductId(input.getId());
        String[] optionNames = productMapper.getProductOptionNames(productOption);

        // 상품에 속한 옵션이 있다면, 옵션 목록을 조회하여 상품에 설정한다.
        if (optionNames != null && optionNames.length > 0) {
            Map<String, List<ProductOption>> options = new HashMap<>();

            for (String optionName : optionNames) {
                ProductOption option = new ProductOption();
                option.setProductId(input.getId());
                option.setName(optionName);

                // 옵션 이름을 기준으로 상품옵션 목록을 조회한다.
                List<ProductOption> optionList = productMapper.getProductOptionsByName(option);

                // 조회된 옵션 목록을 상품에 설정한다.
                options.put(optionName, optionList);
            }

            // 상품 정보에 옵션을 설정한다.
            product.setOptions(options);
        }

        // 조회된 상품 상세 정보 반환
        return product;
    }
}