/**
 * /src/main/java/kr/hossam/myshop/MyWebConfig.java
 */
package kr.hossam.myshop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import kr.hossam.myshop.interceptors.MyInterceptor;

@Configuration
@RequiredArgsConstructor
public class MyWebConfig implements WebMvcConfigurer {

    private final MyInterceptor myInterceptor;

    /** 업로드 된 파일이 저장될 경로 (application.properties로부터 읽어옴) */
    // --> import org.springframework.beans.factory.annotation.Value;
    @Value("${upload.dir}")
    private String uploadDir;

    /** 업로드 된 파일이 노출될 URL 경로 (application.properties로부터 읽어옴) */
    @Value("${upload.url}")
    private String uploadUrl;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 직접 정의한 MyInterceptor를 Spring에 등록
        InterceptorRegistration ir = registry.addInterceptor(myInterceptor);
        // 해당 경로는 인터셉터가 가로채지 않는다.
        ir.excludePathPatterns("/error", "/robots.txt", "/favicon.ico", "/assets/**");
    }

    /** 설정파일에 명시된 업로드 저장 경로와 URL상의 경로를 맵핑 시킴 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(String.format("%s/**", uploadUrl))
                .addResourceLocations(String.format("file://%s/", uploadDir));
    }
}