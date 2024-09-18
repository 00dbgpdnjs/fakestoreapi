package com.example.fakestoreapi.security.jwt.util;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class IfLoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(IfLogin.class) != null
                && parameter.getParameterType() == LoginUserDto.class;
    }

    // TODO : security - 갈아엎기
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        /* 임의로 세팅할 값
            {
                "email": "user@example.com",
                "name": "John Doe",
                "memberId": 123,
                "roles": ["ROLE_USER", "ROLE_ADMIN"]
            }
         */

        LoginUserDto loginUserDto = new LoginUserDto();
//        loginUserDto.setEmail("admin@admin.com");
        loginUserDto.setMemberId(123L); // 아직은 memberId 만 쓰임
//        loginUserDto.setName("hye won");

//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        while (iterator.hasNext()) {
//            GrantedAuthority grantedAuthority = iterator.next();
//            String role = grantedAuthority.getAuthority();
////            System.out.println(role);
//            loginUserDto.addRole(role);
//        }

//        loginUserDto.addRole("ROLE_USER");

        return loginUserDto;
    }
}