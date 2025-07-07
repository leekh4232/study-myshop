package kr.hossam.myshop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import kr.hossam.myshop.helpers.RestApiTestHelper;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerTests {
    @Autowired
    private RestApiTestHelper restApiTestHelper;

    /** 아이디 중복 체크 API 테스트 */
    @Test
    void idUniqueCheck() throws Exception {
        Map<String, Object> params = Map.of("user_id", "testuser123");
        restApiTestHelper.test("GET", "/api/account/id_unique_check", params, null);
    }

    /** 이메일 중복 체크 API 테스트 */
    @Test
    void emailUniqueCheck() throws Exception {
        Map<String, Object> params = Map.of("email", "testuser@gmail.com");
        restApiTestHelper.test("GET", "/api/account/email_unique_check", params, null);
    }

    /** 회원가입 API 테스트 */
    @Test
    void join() throws Exception {
        // 1. 업로드할 파일 구성
        String inputFieldName = "photo"; //파일 업로드 파라미터 이름
        String fileType = "image/jpeg"; //파일타입
        String filePath = "/Users/leekh/Desktop/test.png"; //파일경로
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        MockMultipartFile image1 = new MockMultipartFile(
                inputFieldName,
                file.getName(),
                fileType,
                fileInputStream
        );

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", "hellotest1");
        params.put("user_pw", "1234");
        params.put("user_pw_re", "1234");
        params.put("user_name", "헬로테스트");
        params.put("email", "hellotest1@naver.com");
        params.put("phone", "01012345678");
        params.put("birthday", "2025-07-03");
        params.put("gender", "M");
        params.put("postcode", "12345");
        params.put("addr1", "서울시 어딘가");
        params.put("addr2", "어디이겠지");

        restApiTestHelper.test("POST", "/api/account/join", params, image1);
    }

    /** 로그인 API 테스트 */
    @Test
    void login() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", "hellotest1");
        params.put("user_pw", "1234");

        restApiTestHelper.test("POST", "/api/account/login", params, null);
    }

    /** 로그아웃 테스트 */
    @Test
    void logout() throws Exception {
        restApiTestHelper.test("GET", "/api/account/logout", null, null);
    }

    /** 아이디 찾기 테스트 */
    @Test
    void findId() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_name", "테스트유저1");
        params.put("email", "leekh4232@gmail.com");

        restApiTestHelper.test("POST", "/api/account/find_id", params, null);
    }

    /** 비밀번호 재발급 테스트 */
    @Test
    void resetPw() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", "hellotest1");
        params.put("email", "leekh4232@gmail.com");

        restApiTestHelper.test("PUT", "/api/account/reset_pw", params, null);
    }
}