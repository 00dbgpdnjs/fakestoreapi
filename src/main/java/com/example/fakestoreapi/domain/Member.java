package com.example.fakestoreapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity // Database Table과 맵핑하는 객체.
@Table(name="member")
@NoArgsConstructor // 기본생성자
@Setter
@Getter
public class Member {
    @Id // PK.
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 255, unique = true)
    private String email;

    @Column(length = 50)
    private String name;

    /* JSON 직렬화 및 역직렬화 과정에서 해당 필드 무시
        Member 객체 JSON으로 직렬화 :
            {
                // ...
                "email": "john_doe@goole.com"
                // "password" 필드는 포함되지 않음
                / ...
            }
    *.
     */
    @JsonIgnore
    @Column(length = 500)
    private String password;

    @CreationTimestamp // 현재시간이 저장될 때 자동으로 생성.
    private LocalDateTime regdate;

    @Column(nullable = false)
    private Integer birthYear;

    @Column(nullable = false)
    private Integer birthMonth;

    @Column(nullable = false)
    private Integer birthDay;

    @Column(length = 10, nullable = false)
    private String gender;

    @ManyToMany
    @JoinTable(name = "member_role", // member_role 중간 테이블이 생성 -> 각 Member와 Role 간의 관계를 매핑
            joinColumns = @JoinColumn(name = "member_id"), // 현재 엔티티(주 테이블)의 외래 키
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "memberId=" + memberId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", regdate=" + regdate +
                ", birthYear=" + birthYear +
                ", birthMonth=" + birthMonth +
                ", birthDay=" + birthDay +
                ", gender='" + gender + '\'' +
                '}';
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}

// User -----> Role
