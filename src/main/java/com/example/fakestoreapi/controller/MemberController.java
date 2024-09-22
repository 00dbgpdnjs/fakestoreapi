package com.example.fakestoreapi.controller;

import com.example.fakestoreapi.domain.Member;
import com.example.fakestoreapi.dto.MemberSignupDto;
import com.example.fakestoreapi.dto.MemberSignupResponseDto;
import com.example.fakestoreapi.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
// 입력값 검증 - 주로 메서드 파라미터로 전달된 객체의 유효성 검사
//   검증할 객체(DTO)에 제약 조건을 정의
//   @Valid와 유사하지만 그룹 기반 검증 지원
@Validated
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    /* Could not autowire. No beans of 'PasswordEncoder' type found.
        PasswordEncoder 인터페이스의 구현체를 빈으로 등록
        구현체: BCryptPasswordEncoder, NoOpPasswordEncoder 등

     */
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid MemberSignupDto memberSignupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Member member = new Member();
        member.setName(memberSignupDto.getName());
        member.setEmail(memberSignupDto.getEmail());
        member.setPassword(passwordEncoder.encode(memberSignupDto.getPassword()));
        member.setBirthYear(Integer.parseInt(memberSignupDto.getBirthYear()));
        member.setBirthMonth(Integer.parseInt(memberSignupDto.getBirthMonth()));
        member.setBirthDay(Integer.parseInt(memberSignupDto.getBirthDay()));
        member.setGender(memberSignupDto.getGender());

        Member saveMember = memberService.addMember(member);

        MemberSignupResponseDto memberSignupResponse = new MemberSignupResponseDto();
        memberSignupResponse.setMemberId(saveMember.getMemberId());
        memberSignupResponse.setName(saveMember.getName());
        memberSignupResponse.setRegdate(saveMember.getRegdate());
        memberSignupResponse.setEmail(saveMember.getEmail());

        // 회원가입
        return new ResponseEntity(memberSignupResponse, HttpStatus.CREATED);
    }
}
