package com.sns.member.domain.entity;

import com.sns.board.entity.Board;
import com.sns.common.BaseEntity;

import com.sns.member.enums.MemberStatus;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "members")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    @Column(nullable = false, updatable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true, length = 13)
    private String phone;
    private String address;
    private String birthday;
    private String profileImage;

    /**
     * 유저가 자신이 작성한 게시물을 알 수 있어야하므로 일대다 양방향 처리해줘야하며,
     * mappedBy = N의 외래키역할의 필드 값을 지정해줌
     * PERSIST = post 상태변화 전파
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Board> boards = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.WELCOME;
}
