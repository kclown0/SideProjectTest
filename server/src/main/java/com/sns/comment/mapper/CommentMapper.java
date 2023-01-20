package com.sns.comment.mapper;

import com.sns.board.entity.Board;
import com.sns.comment.dto.CommentDto;
import com.sns.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    default Comment commentPostToPost(CommentDto.Post requestBody, Board board) {
        Comment comment = new Comment();
        comment.setComment(requestBody.getComment());
        comment.setBoard(board);
        return comment;
    }

//    Comment commentPostToPost(CommentDto.Board requestBody);
    Comment commentPatchToPost(CommentDto.Patch requestBody);
    CommentDto.Response commentToPostResponse(Comment comment);

    List<CommentDto.Response> commentsToResponse(List<Comment> comments);

}
