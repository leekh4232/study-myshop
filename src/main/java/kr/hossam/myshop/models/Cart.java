package kr.hossam.myshop.models;

import java.util.List;
import lombok.Data;

@Data
public class Cart {
    private int id;
    private String sessionId;   // 세션 ID - 모든 접속자가 부여받는 고유값
    private Integer memberId;   // 회원 ID, 비회원은 null
    private int productId;      // 상품 ID
    List<CartOption> options;
    private String regDate;
    private String editDate;

    public void setOptionJson(String json) {
        // JSON 문자열을 파싱하여 options 필드에 설정하는 로직을 구현
        // 예: this.options = parseJsonToOptions(json);
    }

    public String getOptionJson() {
        // options 필드를 JSON 문자열로 변환하는 로직을 구현
        // 예: return convertOptionsToJson(this.options);
        return "[{\"key\": 123}, {\"key\": 456}]"; // 임시 반환값, 실제 구현 필요
    }
}