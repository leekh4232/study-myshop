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
     * @param input - 상품 조회 조건
     * @return List<Product> - 상품 목록
     * @throws Exception - 예외 발생 시
     */
    public List<Product> getProducts(Product input) throws Exception;

    /**
     * 상품 개수를 조회한다.
     *
     * @param input - 상품 조회 조건
     * @return int - 상품 개수
     * @throws Exception - 예외 발생 시
     */
    public int getProductCount(Product input) throws Exception;

    /**
     * 상품의 상세 정보를 조회한다.
     *
     * @param input - 상품 조회 조건
     * @return Product - 상품 상세 정보
     * @throws Exception - 예외 발생 시
     */
    public Product getProductDetail(Product input) throws Exception;
}