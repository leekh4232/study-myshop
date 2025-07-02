package kr.hossam.myshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 계정 관련 요청을 처리하는 컨트롤러
 */
@Controller
public class AccountController {
    /**
     * 계정 관련 페이지로 이동
     *
     * @return 계정 관련 페이지의 뷰 이름
     */
    @GetMapping({"/", "/account"})
    public String index() {
        return "account/index";
    }

    /**
     * 회원가입 페이지로 이동
     *
     * @return 회원가입 페이지의 뷰 이름
     */
    @GetMapping("/account/join")
    public String join() {
        return "account/join";
    }

    /**
     * 회원가입 결과 페이지로 이동
     *
     * @return 회원가입 결과 페이지의 뷰 이름
     */
    @GetMapping("/account/join_result")
    public String joinResult() {
        return "account/join_result";
    }
}