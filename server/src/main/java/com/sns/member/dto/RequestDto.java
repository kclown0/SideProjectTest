package com.sns.member.dto;


import com.sns.member.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RequestDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        @NotBlank
        @Email
        private String email;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotBlank(message = "공백은 포함될 수 없습니다.")
        private String name;
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰번호는 형식에 맞추어 11자리와 -를 포함시켜야합니다.")
        private String phone;
        @NotBlank(message = "닉네임을 생성해주세요.")
        private String nickname;
        private Integer age;
        private String birthday;
        private String address;
        private String profileImage;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patch {
        private long memberId;
        @NotBlank
        @Email
        private String email;
        @NotBlank(message = "공백은 포함될 수 없습니다.")
        private String name;
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰번호는 형식에 맞추어 11자리와 -를 포함시켜야합니다.")
        private String phone;
        private String nickname;
        private Integer age;
        private String birthday;
        private String address;
        private String profileImage;
        private MemberStatus memberStatus;

        public void setMemberId(long memberId) {
            this.memberId = memberId;
        }
    }
}


