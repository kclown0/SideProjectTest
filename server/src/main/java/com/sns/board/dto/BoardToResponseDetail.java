package com.sns.board.dto;

import com.sns.member.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BoardToResponseDetail {
    private String title;
    private String content;
    private Long boardId;
    private ResponseDto.ResponseMain member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
