package kr.hossam.myshop.controllers.apis;

import kr.hossam.myshop.helpers.RegexHelper;
import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor // <-- Lombok을 사용하여 생성자 주입을 자동으로 생성
public class AccountRestController {

    /** MemberService 객체 */
    private final MemberService memberService;

    /** 정규표현식 검사 객체 */
    private final RegexHelper regexHelper;

    /**
     * 회원 아이디 중복 검사
     *
     * @param userId 검사할 회원 아이디
     * @param memberInfo 현재 로그인 중인 회원 정보 (세션에서 가져옴)
     * @return 중복 여부 결과에 대한 JSON 응답
     */
    @GetMapping("/api/account/id_unique_check")
    public Map<String, Object> idUniqueCheck(@RequestParam("user_id") String userId,
            @SessionAttribute(value = "memberInfo", required = false) Member memberInfo)
            throws Exception {

        // 입력값에 대한 유효성 검사
        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력할 수 있습니다.");

        // 중복 검사에 사용할 Member 객체 생성
        Member input = new Member();
        input.setUserId(userId);

        // 로그인 중이라면 현재 회원의 일련번호를 함께 전달한다.
        if (memberInfo != null) {
            input.setId(memberInfo.getId());
        }

        // 중복 검사 수행
        memberService.isUniqueUserId(input);

        // 결과를 JSON 형태로 반환
        // RestHelper의 sendJson() 메서드는 기본적으로 성공 응답을 반환한다
        return null;
    }

}