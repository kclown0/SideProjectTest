package com.sns.board.entity;

import com.sns.comment.entity.Comment;
import com.sns.common.BaseEntity;

import com.sns.member.domain.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column
//            (nullable = false)
    private String title;

    @Column
//            (nullable = false)
    private String content;

    //멤버와 연관관계 매핑 필요 - member email 보이게 하기
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
