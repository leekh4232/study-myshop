package kr.hossam.myshop.controllers.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class CartRestController {

    /**
     * 장바구니에 상품 추가 API
     * @param productId 상품 ID
     * @param productName 상품명
     * @param optionIds 옵션 ID들 (쉼표로 구분)
     * @param optionNames 옵션명들 (파이프로 구분)
     * @param quantity 수량
     * @param price 단가
     * @return 성공/실패 응답
     */
    @PostMapping("/api/cart/add")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("optionIds") String optionIds,
            @RequestParam("optionNames") String optionNames,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("price") Integer price) {

        // 요청 데이터 로깅
        log.info("=== 장바구니 담기 요청 수신 ===");
        log.info("상품 ID: {}", productId);
        log.info("상품명: {}", productName);
        log.info("옵션 ID들: {}", optionIds);
        log.info("옵션명들: {}", optionNames);
        log.info("수량: {}", quantity);
        log.info("단가: {}", price);

        return null;
    }
}