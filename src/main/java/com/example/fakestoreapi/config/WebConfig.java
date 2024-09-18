package com.example.fakestoreapi.config;

import com.example.fakestoreapi.security.jwt.util.IfLoginArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// Spring MVC 에 대한 설정파일. 웹에대한 설정파일
@Configuration // Bean 정의를 포함할 수 있음
public class WebConfig implements WebMvcConfigurer {
    /*** 1. CORS 설정 ***/
    // Cross Origin Resource Sharing
    // 다른 도메인에서 요청을 허용하거나 거부할 수 있는 정책
    // 프론트 엔드, 백 엔드 개발
    // 프론트 엔드는 3000번 포트 (React.js), 백 엔드는 8080번 포트
    // http://localhost:3000 ---> 8080 api를 호출할 수 있도록 설정.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 설정 적용
                .allowedOrigins("http://localhost:3000") // CORS 요청을 허용할 출처(origin) 정의
//                .allowedOrigins("https://www.fakeshop.com")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "OPTIONS", "DELETE");
//                .allowCredentials(true); // 자격 증명(쿠키, 인증 헤더 등)을 포함하는 CORS 요청을 허용하려면 이 옵션을 활성화해야 합
    }

    /*** 2. 커스텀 HandlerMethodArgumentResolver 등록 ***/
    // HandlerMethodArgumentResolver
    //   컨트롤러 메서드의 파라미터를 처리하는 커스텀 로직을 정의할 수 있게 해쥼
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // IfLoginArgumentResolver
        //   사용자가 로그인된 상태인지 확인하고,
        //   그에 따라 특정 파라미터를 처리하는 커스텀 로직 구현
        resolvers.add(new IfLoginArgumentResolver());
    }
}