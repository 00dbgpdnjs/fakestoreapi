package com.example.fakestoreapi.repository;

import com.example.fakestoreapi.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
