package kr.hossam.myshop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerTests {
    // 테스트 관련 세팅
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void idUniqueCheck() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/account/id_unique_check")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                        .param("user_id", "testuser"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }

    @Test
    void emailUniqueCheck() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/account/email_unique_check")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                        .param("email", "testuser@gmail.com"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태코드: {}", status);
        log.debug("응답 JSON 전문: {}", responseJson);
    }
}