package kr.hossam.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

// 스케쥴러 활성화
// --> import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class MyshopApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MyshopApplication.class, args);
    }

    // WAR 배포를 위한 설정
    @Override
    protected org.springframework.boot.builder.SpringApplicationBuilder configure(
            org.springframework.boot.builder.SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
