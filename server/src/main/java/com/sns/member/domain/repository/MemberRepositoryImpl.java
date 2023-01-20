package com.sns.member.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.member.domain.entity.QMember;
import com.sns.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sns.member.domain.entity.QMember.member;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public List<MemberDto> getMemberByBoardId(Long boardId) {
        return null;
    }

    @Override
    public List<MemberDto> getMemberByCommentId(Long CommentId) {
        return null;
    }
}
