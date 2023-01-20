package com.sns.member.controller;

import com.sns.common.dto.MultiResponseDto;

import com.sns.common.dto.SingleResponseDto;
import com.sns.member.domain.entity.Member;
import com.sns.member.domain.service.MemberService;
import com.sns.member.dto.RequestDto;
import com.sns.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;


    //회원가입 메서드
    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody RequestDto.Post post) {
        Member member = memberService.createMember(mapper.postToMember(post));
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToResponse(member)),HttpStatus.CREATED);
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@Valid @RequestBody RequestDto.Patch patch,Principal principal){
        patch.setMemberId(memberService.findVerifiedMemberByEmail(principal.getName()).getMemberId());
        Member member = memberService.updateMember(mapper.patchToMember(patch));
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToResponse(member)),HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(Principal principal) {
        Member member = memberService.findVerifiedMemberByEmail(principal.getName());
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToResponse(member)),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        List<Member> members = pageMembers.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(mapper.MembersToResponse(members),pageMembers),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{member-id}")
    public ResponseEntity deleteMember( Principal principal) {
        Member member = memberService.findVerifiedMemberByEmail(principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }
}

