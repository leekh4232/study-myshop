/**
 * /src/main/java/kr/hossam/myshop/controllers/apis/CartRestController.java
 */
package kr.hossam.myshop.controllers.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class CartRestController {

    /**
     * 장바구니에 상품 추가 API
     * @param productId 상품 ID
     * @param productName 상품명
     * @param options 옵션들 (`|`로 구분)
     * @return 성공/실패 응답
     */
    @PostMapping("/api/cart/add")
    public Map<String, Object> addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("options") String[] options) {

        // 요청 데이터 로깅
        log.info("=== 장바구니 담기 요청 수신 ===");
        log.info("상품 ID: {}", productId);
        log.info("상품명: {}", productName);
        for (String option : options) {
            log.info("옵션: {}", option);
        }

        return null;
    }
}