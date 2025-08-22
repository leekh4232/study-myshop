/**
 * /src/main/java/kr/hossam/myshop/controllers/apis/CartRestController.java
 */
package kr.hossam.myshop.controllers.apis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.hossam.myshop.models.Cart;
import kr.hossam.myshop.models.CartOption;
import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    /**
     * 장바구니에 상품 추가 API
     *
     * @param productId   상품 ID
     * @param productName 상품명
     * @param options     옵션들 (`|`로 구분)
     *
     * @return 성공/실패 응답
     */
    @PostMapping("/api/cart/add")
    public Map<String, Object> addToCart(
            HttpServletRequest request,
            @RequestParam("productId") int productId,
            @RequestParam("productName") String productName,
            @RequestParam("options") String[] options) throws Exception {

        // 요청 데이터 로깅
        log.info("=== 장바구니 담기 요청 수신 ===");
        log.info("상품 ID: {}", productId);
        log.info("상품명: {}", productName);
        for (String option : options) {
            log.info("옵션: {}", option);
        }

        // 옵션 데이터를 분리해서 List 객체로 저장
        List<CartOption> cartOptionList = new ArrayList<>();

        for (String option : options) {
            String[] parts = option.split("\\|");
            CartOption cartOption = new CartOption();
            Map<String, String> optionValues = new HashMap<>();

            for (String part : parts) {
                int p = part.indexOf(":");
                if (p != -1) {
                    String key = part.substring(0, p);
                    String value = part.substring(p + 1);
                    log.info("옵션 세부사항 - {}: {}", key, value);

                    if (key.equals("id")) {
                        cartOption.setOptionId(Long.parseLong(value));
                    } else if (key.equals("price")) {
                        cartOption.setPrice(Integer.parseInt(value));
                    } else if (key.equals("quantity")) {
                        cartOption.setQuantity(Integer.parseInt(value));
                    } else {
                        // 기타 옵션 값 처리
                        optionValues.put(key, value);
                    }
                }
            }

            cartOption.setOptionValues(optionValues);
            cartOptionList.add(cartOption);
        }

        // 세션 객체 생성
        HttpSession session = request.getSession();

        // 회원정보가 있다면 추출
        Member memberInfo = (Member) session.getAttribute("memberInfo");

        // 장바구니에 저장할 객체 생성
        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setOptions(cartOptionList);
        cart.setSessionId(session.getId());

        if (memberInfo != null) {
            cart.setMemberId(memberInfo.getId());
        }

        Cart output = cartService.addToCart(cart);

        Map<String, Object> map = new HashMap<>();
        map.put("item", output);

        return map;
    }
}