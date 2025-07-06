package kr.hossam.myshop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerTests {
    /** 테스트 관련 세팅 */
    // 서블릿이 SpringBean을 등록하는데 사용되는 객체 --> DI 기능
    @Autowired
    WebApplicationContext webApplicationContext;

    /** 컨트롤러 테스트 객체 */
    @Autowired
    private MockMvc mockMvc;

    /**
     * 모든 테스트 메서드가 실행되기 전에 호출되는 메서드
     * 테스트를 실행하기 전에 MockMvc 객체를 생성함
     */
    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /** 아이디 중복 체크 API 테스트 */
    @Test
    void idUniqueCheck() throws Exception {
        // 1. 요청 생성
        MockHttpServletRequestBuilder requestBuilder = get("/api/account/id_unique_check");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_id", "testuser123");

        // 2. 요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 3. 결과 출력
        //action.andDo(print());

        // 4. 상태 검증
        action.andExpect(status().isOk());

        // 5. 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        // 6. 디버깅 출력
        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }

    /** 이메일 중복 체크 API 테스트 */
    @Test
    void emailUniqueCheck() throws Exception {
        // 1. 요청 생성
        MockHttpServletRequestBuilder requestBuilder = get("/api/account/email_unique_check");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("email", "testuser@gmail.com");

        // 2. 요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 3. (옵션) 결과 출력
        action.andDo(print());

        // 4. 상태 검증
        action.andExpect(status().isOk());

        // 5. 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        // 6. 디버깅 출력
        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }

    /** 회원가입 API 테스트 */
    @Test
    void join() throws Exception {
        // 1. 업로드할 파일 구성
        String inputFieldName = "photo"; //파일 업로드 파라미터 이름
        String fileType = "image/jpeg"; //파일타입
        String filePath = "C:\\Users\\leekh\\Desktop\\1376395131.jpg"; //파일경로
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        MockMultipartFile image1 = new MockMultipartFile(
                inputFieldName,
                file.getName(),
                fileType,
                fileInputStream
        );

        // 1. 요청 생성
        MockMultipartHttpServletRequestBuilder requestBuilder = multipart("/api/account/join");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_id", "hellotest1");
        requestBuilder.param("user_pw", "1234");
        requestBuilder.param("user_pw_re", "1234");
        requestBuilder.param("user_name", "헬로테스트");
        requestBuilder.param("email", "hellotest1@naver.com");
        requestBuilder.param("phone", "01012345678");
        requestBuilder.param("birthday", "2025-07-03");
        requestBuilder.param("gender", "M");
        requestBuilder.param("postcode", "12345");
        requestBuilder.param("addr1", "서울시 어딘가");
        requestBuilder.param("addr2", "어디이겠지");

        // 파일 업로드 파라미터 추가
        requestBuilder.file(image1);

        // 2. 요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 3. (옵션) 결과 출력
        //action.andDo(print());

        // 4. 상태 검증
        action.andExpect(status().isOk());

        // 5. 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        // 6. 디버깅 출력
        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }

    /** 로그인 API 테스트 */
    @Test
    void login() throws Exception {
        // 1. 요청 생성
        MockHttpServletRequestBuilder requestBuilder = post("/api/account/login");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_id", "helloworld");
        requestBuilder.param("user_pw", "1234");

        // 2. 요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 3. (옵션) 결과 출력
        action.andDo(print());

        // 4. 상태 검증
        action.andExpect(status().isOk());

        // 5. 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        // 6. 디버깅 출력
        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }

    /** 로그아웃 테스트 */
    @Test
    void logout() throws Exception {
        // 1. 요청 생성
        MockHttpServletRequestBuilder requestBuilder = get("/api/account/logout");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        // 로그아웃은 파라미터가 필요하지 않음

        // 2. 요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 3. (옵션) 결과 출력
        action.andDo(print());

        // 4. 상태 검증
        action.andExpect(status().isOk());

        // 5. 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        // 6. 디버깅 출력
        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }

    /** 아이디 찾기 테스트 */
    @Test
    void findId() throws Exception {
        // 1. 요청 생성
        MockHttpServletRequestBuilder requestBuilder = post("/api/account/find_id");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_name", "테스트유저1");
        requestBuilder.param("email", "leekh4232@gmail.com");

        // 2. 요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 3. (옵션) 결과 출력
        action.andDo(print());

        // 4. 상태 검증
        action.andExpect(status().isOk());

        // 5. 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        // 6. 디버깅 출력
        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }
}