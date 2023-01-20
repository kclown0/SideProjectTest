package com.sns.board.dto;

import com.sns.member.dto.ResponseDto;
import lombok.*;

import java.security.Principal;
import java.time.LocalDateTime;

//@Data 검색해보기
public class BoardDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private String title;
        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch {
        private Long boardId;
        private String title;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String title;
        private String content;
        private Long boardId;
        private ResponseDto.ResponseMain member;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
