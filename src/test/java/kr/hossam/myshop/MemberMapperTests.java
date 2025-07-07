package kr.hossam.myshop;

import kr.hossam.myshop.mappers.MemberMapper;
import kr.hossam.myshop.models.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class MemberMapperTests {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    void insert() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setUserId("testuser");
        input.setUserPw("testpw123");
        input.setUserName("테스트유저");
        input.setEmail("testuser@example.com");
        input.setPhone("010-1234-5678");
        input.setBirthday("1990-01-01");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동 101호");
        input.setPhoto(null);

        // insert 실행
        int insertedId = memberMapper.insert(input);
        log.debug("insertedId:{}", insertedId);
    }

    @Test
    void selectItem() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setId(5); // 존재하는 회원의 ID로 설정

        // selectItem 실행
        Member output = memberMapper.selectItem(input);
        log.debug("output:{}", output);
    }

    @Test
    void selectCount() {
        // 조건이 일치하는 회원 수 조회
        Member input = new Member();
        input.setUserId("hellouser");
        int output = memberMapper.selectCount(input);
        log.debug("output:{}", output);
    }

    @Test
    void login() {
        // 테스트용 로그인 정보 생성
        Member input = new Member();
        input.setUserId("testuser");
        input.setUserPw("testpw123");

        // login 실행
        Member output = memberMapper.login(input);
        log.debug("output:{}", output);
    }

    @Test
    void updateLoginDate() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setId(5); // 존재하는 회원의 ID로 설정

        // updateLoginDate 실행
        int output = memberMapper.updateLoginDate(input);
        log.debug("output:{}", output);
    }

    @Test
    void findId() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setUserName("테스트유저1");
        input.setEmail("leekh4232@gmail.com");

        // findId 실행
        Member output = memberMapper.findId(input);
        log.debug("output:{}", output);
    }

    @Test
    void resetPassword() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setUserId("testuser1");
        input.setEmail("leekh4232@gmail.com");
        input.setUserPw("1234"); // 새 비밀번호 설정

        // resetPassword 실행
        int output = memberMapper.resetPw(input);
        log.debug("output:{}", output);
    }

    @Test
<<<<<<< HEAD
    void out() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setId(5);             // 존재하는 회원의 ID로 설정
        input.setUserPw("1234");    // 탈퇴할 회원의 비밀번호

        // out 실행
=======
    void outMember() {
        // 테스트용 회원 정보 생성
        Member input = new Member();
        input.setId(5);             // 존재하는 회원의 ID로 설정
        input.setUserPw("1234");    // 탈퇴할 때 사용할 비밀번호

        // outMember 실행
>>>>>>> 18a7365c3d91793ca0c77b1295e1dee361445f71
        int output = memberMapper.out(input);
        log.debug("output:{}", output);
    }
}