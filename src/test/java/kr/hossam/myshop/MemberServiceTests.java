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
}