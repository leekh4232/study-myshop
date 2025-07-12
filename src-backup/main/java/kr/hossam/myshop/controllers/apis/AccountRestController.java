package kr.hossam.myshop.controllers.apis;

import jakarta.servlet.http.HttpSession;
import kr.hossam.myshop.exceptions.StringFormatException;
import kr.hossam.myshop.helpers.FileHelper;
import kr.hossam.myshop.helpers.MailHelper;
import kr.hossam.myshop.helpers.RegexHelper;
import kr.hossam.myshop.helpers.RestHelper;
import kr.hossam.myshop.helpers.SessionCheckHelper;
import kr.hossam.myshop.helpers.UtilHelper;
import kr.hossam.myshop.models.Member;
import kr.hossam.myshop.models.UploadItem;
import kr.hossam.myshop.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor // <-- Lombok을 사용하여 생성자 주입을 자동으로 생성
public class AccountRestController {

    /** MemberService 객체 */
    private final MemberService memberService;

    /** RestHelper 객체 */
    private final RestHelper restHelper;

    /** 정규표현식 검사 객체 */
    private final RegexHelper regexHelper;

    /** FileHelper 객체 */
    private final FileHelper fileHelper;

    /** UtilHelper 객체 */
    private final UtilHelper utilHelper;

    /** MailHelper 객체 */
    private final MailHelper mailHelper;

    /**
     * 회원 아이디 중복 검사
     *
     * @param userId 검사할 회원 아이디
     * @param memberInfo 현재 로그인 중인 회원 정보 (세션에서 가져옴)
     * @return 중복 여부 결과에 대한 JSON 응답
     */
    @GetMapping("/api/account/id_unique_check")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public Map<String, Object> idUniqueCheck(@RequestParam("user_id") String userId,
            @Parameter(hidden=true) @SessionAttribute(value = "memberInfo", required = false) Member memberInfo)
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
        return restHelper.sendJson();
    }

    /**
     * 회원 이메일 중복 검사
     *
     * @param email 검사할 회원 이메일
     * @param memberInfo 현재 로그인 중인 회원 정보 (세션에서 가져옴)
     * @return 중복 여부 결과에 대한 JSON 응답
     */
    @GetMapping("/api/account/email_unique_check")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public Map<String, Object> EmailUniqueCheck(@RequestParam("email") String email,
            @Parameter(hidden=true) @SessionAttribute(value = "memberInfo", required = false) Member memberInfo)
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
        // RestHelper의 sendJson() 메서드는 기본적으로 성공 응답을 반환한다
        return restHelper.sendJson();
    }

    /**
     * 회원 가입 처리
     *
     * @param userId 회원 아이디
     * @param userPw 회원 비밀번호
     * @param userPwRe 회원 비밀번호 확인
     * @param userName 회원 이름
     * @param email 회원 이메일
     * @param phone 회원 전화번호
     * @param birthday 회원 생년월일
     * @param gender 회원 성별 (M: 남성, F: 여성)
     * @param postcode 회원 우편번호
     * @param addr1 회원 주소1
     * @param addr2 회원 주소2
     * @param photo 회원 프로필 사진 (선택 사항)
     * @return 회원 가입 결과에 대한 JSON 응답
     * @throws Exception 입력값 유효성 검사 실패 또는 회원 가입 처리 중 예외 발생 시
     */
    @PostMapping("/api/account/join")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public Map<String, Object> join(@RequestParam("user_id") String userId,
            @RequestParam("user_pw") String userPw, @RequestParam("user_pw_re") String userPwRe,
            @RequestParam("user_name") String userName, @RequestParam("email") String email,
            @RequestParam("phone") String phone, @RequestParam("birthday") String birthday,
            @RequestParam("gender") String gender, @RequestParam("postcode") String postcode,
            @RequestParam("addr1") String addr1, @RequestParam("addr2") String addr2,
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
        return restHelper.sendJson();
    }

    /**
     * 회원 로그인 처리
     *
     * @param userId 회원 아이디
     * @param userPw 회원 비밀번호
     * @return 로그인 결과에 대한 JSON 응답
     * @throws Exception 입력값 유효성 검사 실패 또는 로그인 처리 중 예외 발생 시
     */
    @PostMapping("/api/account/login")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public Map<String, Object> login(HttpServletRequest request,
            @RequestParam("user_id") String userId, @RequestParam("user_pw") String userPw)
            throws Exception {

        // 입력값에 대한 유효성 검사
        regexHelper.isValue(userId, "아이디를 입력하세요.");
        regexHelper.isValue(userPw, "비밀번호를 입력하세요.");

        // 로그인 정보 설정
        Member input = new Member();
        input.setUserId(userId);
        input.setUserPw(userPw);

        // 로그인 처리
        Member output = memberService.login(input);

        // 세션에 회원 정보 저장
        request.getSession().setAttribute("memberInfo", output);

        // 결과 반환
        return restHelper.sendJson();
    }

    /**
     * 로그아웃 처리
     *
     * @param request HTTP 요청 객체
     * @return 로그아웃 결과에 대한 JSON 응답
     */
    @GetMapping("/api/account/logout")
    @SessionCheckHelper(enable = true) // <-- 로그인 상태에서만 접근 가능함
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return restHelper.sendJson();
    }

    /**
     * 회원 아이디 찾기
     *
     * @param userName 회원 이름
     * @param email 회원 이메일
     * @return 찾은 회원 아이디에 대한 JSON 응답
     * @throws Exception 입력값 유효성 검사 실패 또는 아이디 찾기 처리 중 예외 발생 시
     */
    @PostMapping("/api/account/find_id")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public Map<String, Object> findId(@RequestParam("user_name") String userName,
            @RequestParam("email") String email) throws Exception {

        // 입력값에 대한 유효성 검사
        regexHelper.isValue(userName, "이름을 입력하세요.");
        regexHelper.isValue(email, "이메일을 입력하세요.");
        regexHelper.isEmail(email, "이메일 형식이 잘못되었습니다.");

        // 아이디 찾기에 사용할 Member 객체 생성
        Member input = new Member();
        input.setUserName(userName);
        input.setEmail(email);

        // 아이디 찾기 처리
        Member output = memberService.findId(input);

        // 결과 반환
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("user_id", output.getUserId());

        return restHelper.sendJson(data);
    }

    /**
     * 아이디와 이메일이 일치하는 회원의 비밀번호를 재설정하고 임시 비밀번호를 이메일로 발송한다.
     *
     * @param userId 회원 아이디
     * @param email 회원 이메일
     * @return 비밀번호 재설정 결과에 대한 JSON 응답
     */
    @PutMapping("/api/account/reset_pw")
    @SessionCheckHelper(enable = false) // <-- 로그인되지 않은 상태에서만 접근 가능함
    public Map<String, Object> resetPw(@RequestParam("user_id") String userId,
            @RequestParam("email") String email) throws Exception {

        /** 1) 임시 비밀번호를 DB에 갱신하기 */
        String newPassword = utilHelper.getRandomString(8);
        Member input = new Member();
        input.setUserId(userId);
        input.setEmail(email);
        input.setUserPw(newPassword);

        memberService.resetPw(input);

        /** 2) 이메일 발송을 위한 템플릿 처리 */
        // 메일 템플릿 파일 경로
        ClassPathResource resource = new ClassPathResource("mail_templates/reset_pw.html");
        String mailTemplatePath = resource.getFile().getAbsolutePath();

        // 메일 템플릿 파일 가져오기
        String template = fileHelper.readString(mailTemplatePath);

        // 메일 템플릿 안의 치환자 처리
        template = template.replace("{{userId}}", userId);
        template = template.replace("{{password}}", newPassword);

        /** 3) 메일 발송 */
        String subject = userId + "님의 비밀번호가 재설정 되었습니다.";
        mailHelper.sendMail(email, subject, template);

        return restHelper.sendJson();
    }

    /**
     * 회원 탈퇴 처리
     * @param request       HTTP 요청 객체
     * @param memberInfo    현재 로그인 중인 회원 정보 (세션에서 가져옴)
     * @param password      탈퇴할 때 사용할 비밀번호
     * @return              탈퇴 결과에 대한 JSON 응답
     * @throws Exception    탈퇴 처리 중 예외 발생 시
     */
    @DeleteMapping("/api/account/out")
    @SessionCheckHelper(enable = true) // <-- 로그인 상태에서만 접근 가능함
    public Map<String, Object> out(
        HttpServletRequest request,
        @Parameter(hidden=true) @SessionAttribute("memberInfo") Member memberInfo,
        @RequestParam("password") String password) throws Exception {

        // 세션으로부터 추출한 Member객체에 입력받은 비밀번호를 넣어준다.
        memberInfo.setUserPw(password);

        // 탈퇴 수행
        memberService.out(memberInfo);

        // 로그아웃을 위해 세션을 삭제한다.
        HttpSession session = request.getSession();
        session.invalidate();

        return restHelper.sendJson();
    }

    @PutMapping("/api/account/edit")
    @SessionCheckHelper(enable = true) // <-- 로그인 상태에서만 접근 가능함
    public Map<String, Object> edit(
            HttpServletRequest request,                                     // 세션 갱신용
            @Parameter(hidden=true) @SessionAttribute("memberInfo") Member memberInfo,              // 현재 세션 정보 확인용
            @RequestParam("user_pw") String userPw,                         // 현재 비밀번호 (정보 확인용)
            @RequestParam("new_user_pw") String newUserPw,                  // 신규 비밀번호 (정보 변경용)
            @RequestParam("new_user_pw_confirm") String newUserPwConfirm,   // 신규 비밀번호 확인값
            @RequestParam("user_name") String userName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("birthday") String birthday,
            @RequestParam("gender") String gender,
            @RequestParam("postcode") String postcode,
            @RequestParam("addr1") String addr1,
            @RequestParam("addr2") String addr2,
            @RequestParam(value="delete_photo", defaultValue="N") String deletePhoto,
            @RequestParam(value = "photo", required = false) MultipartFile photo)
            throws Exception {

        /** 1) 입력값에 대한 유효성 검사 */
        regexHelper.isValue(userPw, "현재 비밀번호를 입력하세요.");

        if ((newUserPw != null && !newUserPw.isEmpty()) && !newUserPw.equals(newUserPwConfirm)) {
            // 신규 비밀번호가 입력된 경우
            regexHelper.isValue(newUserPw, "비밀번호 확인이 잘못되었습니다.");
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

        /** 2) 이메일 중복 검사 */
        Member input = new Member();
        input.setEmail(email);
        input.setId(memberInfo.getId());
        memberService.isUniqueEmail(input);

        /** 3) 업로드 처리 */
        UploadItem uploadItem = null;

        try {
            uploadItem = fileHelper.saveMultipartFile(photo);
        } catch (Exception e) {
            // FileHelper는 upload field 자체가 없는 경우 예외를 발생시킨다.
            // 하지만 html에서 사진을 삭제하지 않는 경우에는 upload 필드가 disabled 되므로
            // 컨트롤러 입장에서는 업로드 필드 자체가 없는 경우로 판단되기 때문에 에러가 발생한다.
            // 이 경우는 에러로 처리하면 안되므로 catch 블록을 만들고 예외를 무시하기 위해 비워 놓는다.
        }

        /** 4) 정보를 Service에 전달하기 위한 객체 구성 */
        // 아이디는 수정할 필요가 없으므로 설정하지 않는다.
        Member member = new Member();
        member.setId(memberInfo.getId());   // 중요: WHERE절에 사용할 PK설정
        member.setUserPw(userPw);           // 현재 비밀번호 --> WHERE절 사용
        member.setNewUserPw(newUserPw);     // 신규 비밀번호는 --> 값이 있을 경우만 갱신
        member.setUserName(userName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setBirthday(birthday);
        member.setGender(gender);
        member.setPostcode(postcode);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        // 현재 프로필 사진값을 가져온다.
        String currentPhoto = memberInfo.getPhoto();

        // 현재 프로필 사진이 있는 경우
        if (currentPhoto != null && !currentPhoto.equals("")) {
            // 기존 사진의 삭제가 요청되었다면?
            if (deletePhoto.equals("Y")) {

                try {
                    fileHelper.deleteFile(currentPhoto);
                } catch (Exception e) {
                    // 파일 삭제에 실패한 경우 --> 예외를 발생시키지 않고 그냥 넘어간다.
                }

                // 업로드 된 사진이 있다면 Beans에 포함한다.
                // 기존 파일이 있을 경우에는 삭제없이는 정보를 갱신하면 안된다.
                if (uploadItem != null) {
                    member.setPhoto(uploadItem.getFilePath());
                } else {
                    // 삭제만 하고 새로운 파일은 업로드 하지 않은 경우
                    // --> Member 클래스에서 photo는 String
                    // --> 기본값이 null이란 의미
                    // --> 별도로 처리하지 않는 한 member객체의 photo는 null이란 의미
                    member.setPhoto(null);
                }
            } else {
                // 삭제 요청이 없는 경우는 세션의 사진 경로(=기존 정보)를 그대로 적용하여
                // 기존 사진을 유지하도록 한다.
                member.setPhoto(currentPhoto);
            }
        } else {
            // 업로드 된 사진이 있다면 Beans에 포함한다.
            if (uploadItem != null) {
                member.setPhoto(uploadItem.getFilePath());
            }
        }

        /** 5) DB에 저장 */
        Member output = memberService.update(member);

        /** 6) 변경된 정보로 세션 갱신 */
        request.getSession().setAttribute("memberInfo", output);

        return restHelper.sendJson();
    }
}
