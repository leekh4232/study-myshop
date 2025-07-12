package kr.hossam.myshop.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestComponent
public class RestApiTestHelper {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 요청 실행, 상태 검증, 결과 출력, JSON/Status 로그 출력까지 한 번에 처리
     *
     * @param method 요청 방식 (GET, POST, PUT, DELETE)
     * @param url 요청 URL
     * @param params 파라미터 맵
     * @throws Exception
     */
    public void test(String method, String url, Map<String, Object> params, MockMultipartFile file)
            throws Exception {

        MockHttpServletRequestBuilder builder;

        if (file != null) {
            // 파일 업로드가 필요한 경우 multipart 사용
            builder = multipart(url).file(file);
        } else {
            switch (method.toUpperCase()) {
                case "GET":
                    builder = get(url);
                    break;
                case "POST":
                    builder = post(url);
                    break;
                case "PUT":
                    builder = put(url);
                    break;
                case "DELETE":
                    builder = delete(url);
                    break;
                default:
                    throw new IllegalArgumentException("지원하지 않는 HTTP 메서드: " + method);
            }
        }

        // 파라미터 추가
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    builder.param(entry.getKey(), entry.getValue().toString());
                }
            }
        }

        // 기본 설정
        builder.accept(MediaType.APPLICATION_JSON);
        builder.header("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");

        // 요청 실행
        ResultActions action = mockMvc.perform(builder);

        // (옵션) 결과 출력
        action.andDo(print());

        // 상태 검증
        action.andExpect(status().isOk());

        // 결과 추출 및 로그 출력
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }
}