package com.sns.member.domain.repository;

import com.sns.member.dto.MemberDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberDto> getMemberByBoardId(Long boardId);  //특정 게시물을 작성한 특정 유저를 조회한다.
    List<MemberDto> getMemberByCommentId(Long CommentId);  //특정 댓글을 작성한 특정 유저를 조회한다.
}
