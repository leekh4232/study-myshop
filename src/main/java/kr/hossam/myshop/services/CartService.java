package kr.hossam.myshop.services;

import kr.hossam.myshop.models.Cart;

public interface CartService {
    /**
     * 장바구니에 상품을 추가한다.
     *
     * @param input Cart 객체 - 장바구니 정보
     * @return Cart - 추가된 장바구니 정보
     * @throws Exception - 예외 발생 시
     */
    public Cart addToCart(Cart input) throws Exception;
}