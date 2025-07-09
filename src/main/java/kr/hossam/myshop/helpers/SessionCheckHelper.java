package kr.hossam.myshop.helpers;

import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionCheckHelper {
    boolean enable() default true;
}
