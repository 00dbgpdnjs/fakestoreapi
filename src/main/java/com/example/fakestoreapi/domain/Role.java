package com.example.fakestoreapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="role")
@NoArgsConstructor // JPA에서는 엔티티 클래스가 기본 생성자 필요
@Setter
@Getter
public class Role {
    @Id
    @Column(name="role_id")
    private Long roleId;

    @Column(length = 20)
    private String name;

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
