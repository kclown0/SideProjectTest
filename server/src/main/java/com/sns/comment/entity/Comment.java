package com.sns.comment.entity;

import com.sns.common.BaseEntity;
import com.sns.member.domain.entity.Member;
import com.sns.board.entity.Board;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long commentId;

        @Column
//                (nullable = false)
        private String comment;


        //멤버와 연관관계 매핑 필요 - member email 보이게 하기
        @ManyToOne
        @JoinColumn(name = "member_id")
        private Member member;

        @ManyToOne
        @JoinColumn(name = "board_id")
        private Board board;
}
