package com.sns.member.domain.repository;

import com.sns.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> ,MemberRepositoryCustom {
    Optional <Member> findByEmail(String email);
    Member findByMemberId(Long memberId);
}
