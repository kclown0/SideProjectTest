package com.sns.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"Member Not Found"),
    MEMBER_EXISTS(409,"Member Exists"),
    BOARD_NOT_FOUND(404,"Board Not Found"),
    COMMENT_NOT_FOUND(404,"Comment Not Found"),
    INVALID_TOKEN(401, "Token is expired"),

    UNAUTHORIZED_NOT_USER(401, "You must be logged."),
    EXPIRED_TOKEN(401, "Token is expired"),
    UNAUTHORIZED(401, "Unauthorized"),
    EMPTY_TOKEN(401, "Access Token is empty"),
    REVIEW_NOT_FOUND(409,"sdasdada"),
    REVIEW_EXISTS(444,"sdasadssa");



    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
