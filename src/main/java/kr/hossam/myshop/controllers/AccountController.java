package kr.hossam.myshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import kr.hossam.myshop.helpers.SessionCheckHelper;

/**
 * 계정 관련 요청을 처리하는 컨트롤러
 */
@Controller
public class AccountController {
    /**
     * 계정 관련 페이지 홈
     *
     * @return 계정 관련 페이지의 뷰 이름
     */
    @GetMapping("/account")
    public String index() {
        return "account/index";
    }

    /**
     * 회원가입 페이지로 이동
     *
     * @return 회원가입 페이지의 뷰 이름
     */
    @GetMapping("/account/join")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public String join() {
        return "account/join";
    }

    /**
     * 회원가입 결과 페이지
     *
     * @return 회원가입 결과 페이지의 뷰 이름
     */
    @GetMapping("/account/join_result")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public String joinResult() {
        return "account/join_result";
    }

    /**
     * 로그인 페이지
     *
     * @return 로그인 페이지의 뷰 이름
     */
    @GetMapping("/account/login")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public String login() {
        return "account/login";
    }

    /**
     * 아이디 찾기 페이지
     *
     * @return 아이디 찾기 페이지의 뷰 이름
     */
    @GetMapping("/account/find_id")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public String findId() {
        return "account/find_id";
    }

    /**
     * 비밀번호 재설정 페이지
     *
     * @return 비밀번호 재설정 페이지의 뷰 이름
     */
    @GetMapping("/account/reset_pw")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public String resetPw() {
        return "account/reset_pw";
    }

    /**
     * 회원 정보 수정 페이지
     *
     * @return 회원 정보 수정 페이지의 뷰 이름
     */
    @GetMapping("/account/edit")
    @SessionCheckHelper(enable = true) // <-- 로그인된 상태에서만 접근 가능함
    public String edit() {
        return "account/edit";
    }

    /**
     * 회원 탈퇴 페이지
     *
     * @return 회원 탈퇴 페이지의 뷰 이름
     */
    @GetMapping("/account/out")
    @SessionCheckHelper(enable = true) // <-- 로그인된 상태에서만 접근 가능함
    public String out() {
        return "account/out";
    }
}