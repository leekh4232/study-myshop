package kr.hossam.myshop.helpers;

import java.lang.annotation.*;

/**
 * 로그인 체크를 수행해야 하는 페이지인지 여부를 설정하는 어노테이션.
 * 이 어노테이션이 적용된 메서드는 세션 체크를 수행합니다.
 *
 * enable = true --> 로그인을 해야만 접근 가능한 페이지
 * enable = false --> 로그인을 하지 않아야만 접근 가능한 페이지
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionCheckHelper {
    boolean enable() default true;
}
