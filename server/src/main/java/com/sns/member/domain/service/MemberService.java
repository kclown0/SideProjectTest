package com.sns.member.domain.service;

import com.sns.security.utils.CustomAuthorityUtils;
import com.sns.common.exception.BusinessLogicException;
import com.sns.common.exception.ExceptionCode;
import com.sns.member.domain.entity.Member;
import com.sns.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member savedMember = memberRepository.save(member);
        savedMember.setCreatedAt(LocalDateTime.now());
        return savedMember;
    }


    public Member updateMember(Member member) {
        Member patchMember = memberRepository.findByMemberId(member.getMemberId());

        Optional.ofNullable(member.getName())
                .ifPresent(name -> patchMember.setName(name));
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> patchMember.setPassword(passwordEncoder.encode(member.getPassword())));
        Optional.ofNullable(member.getPhone())
                .ifPresent(phone -> patchMember.setPhone(phone));
        Optional.ofNullable(member.getAddress())
                .ifPresent(address -> patchMember.setAddress(address));
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(memberStatus -> patchMember.setMemberStatus(memberStatus));

        patchMember.setModifiedAt(LocalDateTime.now());
        return memberRepository.save(patchMember);

    }
    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return findVerifiedMemberByEmail(email);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }


    public void deleteMember(String email) {
        Member findMember = findVerifiedMemberByEmail(email);
        memberRepository.delete(findMember);
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        Member findMember = member.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }


    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}