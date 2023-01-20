package com.sns.member.enums;

import lombok.Getter;

public enum MemberStatus {
    WELCOME("회원가입을 축하드립니다."),
    DONE("완료되었습니다."),
    QUIT("회원이 아닙니다.");

    @Getter
    private String status;

    MemberStatus(String status) {
        this.status = status;
    }
}
