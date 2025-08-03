/**
 * /src/main/java/kr/hossam/myshop/services/ProductService.java
 */
package kr.hossam.myshop.services;

import kr.hossam.myshop.models.Product;

import java.util.List;

public interface ProductService {

    /**
     * 상품 목록을 조회한다.
     *
     * @return List<Product> - 상품 목록
     * @throws Exception - 예외 발생 시
     */
    public List<Product> getProducts(Product input) throws Exception;
}