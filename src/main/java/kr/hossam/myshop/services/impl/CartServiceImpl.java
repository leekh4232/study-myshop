package kr.hossam.myshop.services.impl;

import kr.hossam.myshop.mappers.CartMapper;
import kr.hossam.myshop.models.Cart;
import kr.hossam.myshop.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Override
    public Cart addToCart(Cart input) throws Exception {
        int output = cartMapper.insertCart(input);
        return cartMapper.selectCartItemById(output);
    }
}