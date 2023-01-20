package com.sns.member.mapper;

import com.sns.member.domain.entity.Member;
import com.sns.member.dto.RequestDto;
import com.sns.member.dto.ResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member postToMember(RequestDto.Post post);
    Member patchToMember(RequestDto.Patch patch);
    ResponseDto.Response memberToResponse(Member member);

    ResponseDto.ResponseMain memberToResponseMain(Member member);
    List<ResponseDto.Response> MembersToResponse(List<Member> members);

}
