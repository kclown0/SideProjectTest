package com.sns.board.mapper;


import com.sns.board.dto.BoardCommentPageInfoResponseDto;
import com.sns.board.dto.BoardDto;
import com.sns.board.dto.BoardListDto;
import com.sns.board.dto.BoardToResponseDetail;
import com.sns.board.entity.Board;
import com.sns.comment.entity.Comment;
import com.sns.comment.mapper.CommentMapper;
import com.sns.comment.service.CommentService;
import com.sns.common.dto.PageInfo;
import com.sns.member.mapper.MemberMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
        Board boardPostToPost(BoardDto.Post requestBody);
        Board boardPatchToPost(BoardDto.Patch requestBody);

        BoardDto.Response boardToPostResponse(Board board);

        default BoardToResponseDetail boardToResponseDetail(Board board, MemberMapper memberMapper){
                return BoardToResponseDetail.builder()
                        .boardId(board.getBoardId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .createdAt(board.getCreatedAt())
                        .modifiedAt(board.getModifiedAt())
                        .member(memberMapper.memberToResponseMain(board.getMember()))
                        .build();
        }

        default BoardCommentPageInfoResponseDto DetailToBoardCommentPageInfo(BoardToResponseDetail boardToResponseDetail,
                                                                             CommentService commentService, CommentMapper commentMapper,
                                                                             Integer commentPage, Integer commentSize,
                                                                             Board board) {

                Page<Comment> comments = commentService.readComments(board, commentPage, commentSize);
                List<Comment> commentList = comments.getContent();

                System.out.println(comments.getTotalPages());

                return  BoardCommentPageInfoResponseDto.builder()
                        .data(boardToResponseDetail)
                        .comments(commentMapper.commentsToResponse(commentList))
                        .pageInfo(new PageInfo(comments.getNumber() + 1, comments.getSize(), comments.getTotalElements(), comments.getTotalPages()))
                        .build();
        }


        default List<BoardListDto> boardsToPostResponseDto(List<Board> boardList) {
                return boardList.stream()
                        .map(board -> BoardListDto
                        .builder()
                                .boardId(board.getBoardId())
                                .title(board.getTitle())
                                .content(board.getContent())
                                .createdAt(board.getCreatedAt())
                                .modifiedAt(board.getModifiedAt())
                                .email(board.getMember().getEmail())
                                .build())
                                .collect(Collectors.toList());
        };


}
