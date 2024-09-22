package com.example.fakestoreapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// 객체 생성 시 여러 매개변수를 설정할 수 있는 유연한 방법을 제공
//   빌더가 생성된 후에는 필드 값을 변경x
//   일반적으로 setter 제공x
//   객체의 불변성을 유지하기 위해
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupDto {
    @NotEmpty
    // ^: 문자열의 시작
    // @ 앞 부분 : +, -, _, . 도 포함
    // +: 앞의 패턴이 하나 이상 반복될 수 있음
    // $: 문자열의 끝
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식을 맞춰야합니다")
    private String email;

    @NotEmpty
    // (?=.*[A-Za-z]): 전방 탐색을 사용하여 최소 하나의 알파벳이 포함되어야 함
    // (?=.*\d) : 최소 하나의 숫자 포함
    // (?=.[~!@#$%^&()+|=]): 최소 하나의 특수 문자 포함
    // 길이는 7자 이상 16자 이하
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{7,16}$",
            message = "비밀번호는 영문+숫자+특수문자를 포함한 8~20자여야 합니다")
    private String password;

    @NotEmpty
    // \\s: 공백 문자(스페이스, 탭 등)
    @Pattern(regexp = "^[a-zA-Z가-힣\\\\s]{2,15}",
            message = "이름은 영문자, 한글, 공백포함 2글자부터 15글자까지 가능합니다.")
    private String name;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "생년은 4자리 숫자로 입력해야 합니다")
    private String birthYear;

    @NotNull
    // 0?: 0이 0회 또는 1회 나타날 수 있음. 즉, "01"부터 "09"까지의 월을 허용
    // |: OR
    // 1[012]: "10", "11", "12"와 같은 월
        // 1: 1이 나타나야 함
        // [012]: 0, 1, 2 중 하나가 뒤에 따라올 수 있음
    @Pattern(regexp = "^(0?[1-9]|1[012])$", message = "생월은 1부터 12까지의 숫자로 입력해야 합니다")
    private String birthMonth;

    @NotNull
    @Pattern(regexp = "^(0?[1-9]|[12][0-9]|3[01])$", message = "생일은 1부터 31까지의 숫자로 입력해야 합니다")
    private String birthDay;

    @NotEmpty
    @Pattern(regexp = "^[MF]{1}$", message = "성별은 'M' 또는 'F'로 입력해야 합니다")
    private String gender;
}
