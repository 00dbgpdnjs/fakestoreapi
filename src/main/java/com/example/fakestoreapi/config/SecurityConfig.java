package com.example.fakestoreapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/* Spring Security 디펜던시를 추가하면
    기본적으로 모든 요청에 대해 인증이 요구됨 ;모든 URL에 대한 접근이 제한
    따라서 특정 URL에 대한 접근 권한을 설정해야함
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    // todo : Security
//    private final AuthenticationManagerConfig authenticationManagerConfig;
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 세션 관리 설정
                // - 세션을 사용하지 않도록
                //   ;서버가 클라이언트의 상태를 저장x
                //     클라이언트가 서버에 요청할 때마다 새로운 요청으로 간주
                //     이전 요청의 상태를 기억하지 않습니다.
                // - 주로 RESTful API에서 사용
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 기본 폼 로그인을 비활성화
                // - 보통 토큰 기반 인증을 사용하므로 불필요
                // - 기본 폼 로그인: 사용자가 이름과 비밀번호 입력하여 인증
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                // 주로 API에서는 CSRF 보호 필요x
                .csrf(csrf -> csrf.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                // HTTP 기본 인증 비활성화
                // -보통 토큰 기반 인증이나 세션 기반 인증을 더 많이 사용
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .authorizeHttpRequests(httpRequests -> httpRequests
                        // CORS 요청에 필요한 초기 요청
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // Preflight 요청은 허용한다. https://velog.io/@jijang/%EC%82%AC%EC%A0%84-%EC%9A%94%EC%B2%AD-Preflight-request
                        // 인증 없이 접근 가능
                        .requestMatchers( "/members/signup", "/members/login", "/members/refreshToken").permitAll()
                        .requestMatchers(GET, "/categories/**", "/products/**").permitAll()
                        .requestMatchers(GET,"/**").hasAnyRole( "USER") // 모든 경로(/**)
                        .requestMatchers(POST,"/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().hasAnyRole("USER", "ADMIN")); // 경로: 모든 경로(위에서 명시되지 않은 나머지 요청들)
                // 인증 오류 시 사용할 커스텀 인증 엔트리 포인트를 설정
                // - 이를 통해 인증 실패 시 특정 동작을 정의
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint))
//                // 인증 관리자 설정 적용
//                .apply(authenticationManagerConfig);
        // 설정이 완료된 HttpSecurity 객체를 기반으로 SecurityFilterChain을 빌드하여 반환
        return httpSecurity.build();
    }

    // <<Advanced>> Security Cors로 변경 시도
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true); // 이거 빼면 된다
        // https://gareen.tistory.com/66
        config.addAllowedOrigin("*"); // 어느 도메인에서든지 요청을 받을 수 있다
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTION","PUT")); // 중복 ??
        // /로 시작하는 모든 URL 경로에 대해 앞서 설정한 CORS 정책을 적용
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /* BCrypt
    - 비밀번호를 안전하게 저장하기 위한 해시 함수
      비밀번호 해싱을 위해 Blowfish 암호화 알고리즘을 사용
      암호화된 비밀번호를 저장할 때 임의의 솔트(salt)를 생성하여 비밀번호 보안성 높임
    - 강력한 암호화 알고리즘을 사용하기 때문에 해독이 거의 불가능합니다.
      이는 해커가 데이터베이스를 공격하여 해시된 비밀번호를 복원하는 것을 어렵게 만듦
      높은 보안성을 위해 비밀번호를 반복해서 해싱하는 기능(최소 10회) 지원
    */
    // 암호를 암호화하거나, 사용자가 입력한 암호가 기존 암호랑 일치하는지 검사할 때 이 Bean을 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
