/**
 * /src/main/java/kr/hossam/myshop/models/Product.java
 */
package kr.hossam.myshop.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Product {
    private int id;
    private String name;
    private int price;
    private int discount;
    private String summary;
    private String imageUrl;
    private String deliveryInfo;
    private String mdComment;
    private String productUrl;
    private String content;
    private String regDate;
    private String editDate;

    // 카테고리 테이블에 대한 JOIN을 위한 필드
    private int categoryId;
    private String categoryName;

    /**
     * 한 페이지에 표시될 목록 수
     * MySQL의 Limit절에서 사용할 값이므로 Beans에 추가한다.
     *
     * 1) static 변수로 선언하여 모든 객체가 공유한다.
     * 2) static 변수는 객체를 생성하지 않고도 사용할 수 있다.
     * 3) static 변수에 Lombok을 적용하려면
     *   @Getter, @Setter를 개별적으로 적용한다.
     */
    @Getter
    @Setter
    private static int listCount = 0;

    /**
     * MySQL의 Limit절에 사용될 offset값.
     * MySQL의 Limit절에서 사용할 값이므로 Beans에 추가한다.
     *
     * offset위치부터 listCount만큼의 데이터를 가져온다.
     */
    @Getter
    @Setter
    private static int offset = 0;
}