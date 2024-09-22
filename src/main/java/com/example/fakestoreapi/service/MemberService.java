package com.example.fakestoreapi.service;

import com.example.fakestoreapi.domain.Member;
import com.example.fakestoreapi.domain.Role;
import com.example.fakestoreapi.repository.MemberRepository;
import com.example.fakestoreapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public Member addMember(Member member) {
        // todo : repositody - Spring Boot 애플리케이션이 실행될때  role 들 자동으로 추가되도록 수정
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        member.addRole(userRole.get());
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }
}
