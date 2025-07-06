package kr.hossam.myshop;

import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Test
    void testIsUniqueUserId() {
        // 중복 아이디 검사 - 예외 발생 기대
        Member input = new Member();
        input.setUserId("test");

        try {
            memberService.isUniqueUserId(input);
        } catch (Exception e) {
            log.debug("사용할 수 없는 아이디 입니다.");
            return;
        }

        log.debug("사용 가능한 아이디 입니다.");
    }

    @Test
    void testIsUniqueEmail() {
        Member input = new Member();
        input.setEmail("test@example.com");

        try {
            memberService.isUniqueEmail(input);
        } catch (Exception e) {
            log.debug("사용할 수 없는 이메일 입니다.");
            return;
        }

        log.debug("사용 가능한 이메일 입니다.");
    }

    @Test
    void testJoin() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setUserId("helloworld");
        input.setUserPw("1234");
        input.setUserName("헬로월드");
        input.setEmail("helloworld@example.com");
        input.setPhone("010-1234-5678");
        input.setBirthday("1990-01-01");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동 101호");
        input.setPhoto(null);

        Member output = null;

        try {
            output = memberService.join(input);
        } catch (Exception e) {
            log.error("회원 가입에 실패했습니다.", e);
            return;
        }

        log.debug("가입된 회원 정보: {}", output);
    }

    @Test
    void testLogin() {
        // 테스트용 로그인 정보 생성
        Member input = new Member();
        input.setUserId("helloworld");
        input.setUserPw("1234");

        Member output = null;
        try {
            output = memberService.login(input);
        } catch (Exception e) {
            log.error("로그인에 실패했습니다.", e);
            return;
        }

        log.debug("로그인 성공, 회원 정보: {}", output);
    }

    @Test
    void testFindId() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setUserName("테스트유저1");
        input.setEmail("leekh4232@gmail.com");

        Member output = null;
        try {
            output = memberService.findId(input);
        } catch (Exception e) {
            log.error("아이디 찾기에 실패했습니다.", e);
            return;
        }

        log.debug("찾은 아이디: {}", output.getUserId());
    }

    @Test
    void testResetPw() {
        // 테스트용 비밀번호 재설정 정보 생성
        Member input = new Member();
        input.setUserId("testuser1");
        input.setEmail("leekh4232@gmail.com");
        input.setUserPw("1234");

        try {
            memberService.resetPw(input);
        } catch (Exception e) {
            log.error("비밀번호 재설정에 실패했습니다.", e);
            return;
        }

        log.debug("비밀번호가 성공적으로 재설정되었습니다.");
    }
}