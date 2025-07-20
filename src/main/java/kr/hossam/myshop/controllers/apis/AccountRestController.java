package kr.hossam.myshop.controllers.apis;

import kr.hossam.myshop.exceptions.StringFormatException;
import kr.hossam.myshop.helpers.FileHelper;
import kr.hossam.myshop.helpers.RegexHelper;
import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.models.UploadItem;
import kr.hossam.myshop.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor // <-- Lombok을 사용하여 생성자 주입을 자동으로 생성
public class AccountRestController {

    /** MemberService 객체 */
    private final MemberService memberService;

    /** 정규표현식 검사 객체 */
    private final RegexHelper regexHelper;

    /** FileHelper 객체 */
    private final FileHelper fileHelper;

    /**
     * 회원 아이디 중복 검사
     *
     * @param userId     검사할 회원 아이디
     * @param memberInfo 현재 로그인 중인 회원 정보 (세션에서 가져옴)
     *
     * @return 중복 여부 결과에 대한 JSON 응답
     */
    @GetMapping("/api/account/id_unique_check")
    public Map<String, Object> idUniqueCheck(
            @RequestParam("user_id") String userId,
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

    /**
     * 회원 이메일 중복 검사
     *
     * @param email      검사할 회원 이메일
     * @param memberInfo 현재 로그인 중인 회원 정보 (세션에서 가져옴)
     *
     * @return 중복 여부 결과에 대한 JSON 응답
     */
    @GetMapping("/api/account/email_unique_check")
    public Map<String, Object> EmailUniqueCheck(
            @RequestParam("email") String email,
            @SessionAttribute(value = "memberInfo", required = false) Member memberInfo)
            throws Exception {

        // 입력값에 대한 유효성 검사
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 맞지 않습니다.");

        // 중복 검사에 사용할 Member 객체 생성
        Member input = new Member();
        input.setEmail(email);

        // 로그인 중이라면 현재 회원의 일련번호를 함께 전달한다.
        if (memberInfo != null) {
            input.setId(memberInfo.getId());
        }

        // 중복 검사 수행
        memberService.isUniqueEmail(input);

        // 결과를 JSON 형태로 반환
        return null;
    }

    /**
     * 회원 가입 처리
     *
     * @param userId   회원 아이디
     * @param userPw   회원 비밀번호
     * @param userPwRe 회원 비밀번호 확인
     * @param userName 회원 이름
     * @param email    회원 이메일
     * @param phone    회원 전화번호
     * @param birthday 회원 생년월일
     * @param gender   회원 성별 (M: 남성, F: 여성)
     * @param postcode 회원 우편번호
     * @param addr1    회원 주소1
     * @param addr2    회원 주소2
     * @param photo    회원 프로필 사진 (선택 사항)
     *
     * @return 회원 가입 결과에 대한 JSON 응답
     *
     * @throws Exception 입력값 유효성 검사 실패 또는 회원 가입 처리 중 예외 발생 시
     */
    @PostMapping("/api/account/join")
    public Map<String, Object> join(
            @RequestParam("user_id") String userId,
            @RequestParam("user_pw") String userPw,
            @RequestParam("user_pw_re") String userPwRe,
            @RequestParam("user_name") String userName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("birthday") String birthday,
            @RequestParam("gender") String gender,
            @RequestParam("postcode") String postcode,
            @RequestParam("addr1") String addr1,
            @RequestParam("addr2") String addr2,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws Exception {

        /** 1) 입력값에 대한 유효성 검사 */
        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력할 수 있습니다.");
        regexHelper.isValue(userPw, "비밀번호를 입력하세요.");

        if (!userPw.equals(userPwRe)) {
            throw new StringFormatException("비밀번호가 확인이 잘못되었습니다.");
        }

        regexHelper.isValue(userName, "이름을 입력하세요.");
        regexHelper.isKor(userName, "이름은 한글만 입력할 수 있습니다.");
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 잘못되었습니다.");
        regexHelper.isValue(phone, "전화번호를 입력하세요.");
        regexHelper.isPhone(phone, "전화번호 형식이 잘못되었습니다.");
        regexHelper.isValue(birthday, "생년월일을 입력하세요.");
        regexHelper.isValue(gender, "성별을 입력하세요.");

        if (!gender.equals("M") && !gender.equals("F")) {
            throw new StringFormatException("성별은 M(남성) 또는 F(여성)만 입력할 수 있습니다.");
        }

        regexHelper.isValue(postcode, "우편번호를 입력하세요.");
        regexHelper.isValue(addr1, "주소를 입력하세요.");
        regexHelper.isValue(addr2, "상세주소를 입력하세요.");

        /** 2) 업로드 받기 */
        UploadItem uploadItem = fileHelper.saveMultipartFile(photo);

        /** 3) 정보를 Service에 전달하기 위한 객체 구성 */
        Member member = new Member();
        member.setUserId(userId);
        member.setUserPw(userPw);
        member.setUserName(userName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setBirthday(birthday);
        member.setGender(gender);
        member.setPostcode(postcode);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        // 업로드 된 이미지의 경로만 DB에 저장하면 됨
        if (uploadItem != null) {
            member.setPhoto(uploadItem.getFilePath());
        }

        /** 4) DB에 저장 */
        memberService.join(member);

        /** 5) 결과 반환 */
        return null;
    }

}