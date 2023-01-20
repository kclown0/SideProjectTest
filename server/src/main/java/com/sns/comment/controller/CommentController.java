package com.sns.comment.controller;

import com.sns.board.entity.Board;
import com.sns.comment.dto.CommentDto;
import com.sns.comment.entity.Comment;
import com.sns.comment.mapper.CommentMapper;
import com.sns.comment.service.CommentService;
import com.sns.common.dto.SingleResponseDto;
import com.sns.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;

@RestController
@RequestMapping("/v1/boards/") //로그인 시 아이디 넣기
@Validated
@Slf4j
public class CommentController {

    private final BoardRepository boardRepository;
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    public CommentController(CommentService commentService, CommentMapper commentMapper, BoardRepository boardRepository) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.boardRepository = boardRepository;
    }


     @PostMapping("/{board-id}/comments")
        public ResponseEntity postComment(@PathVariable("board-id") Long boardId,
                                         @Valid @RequestBody CommentDto.Post requestBody,
                                         Principal principal) {
            Board board = boardRepository.findByBoardId(boardId);
            Comment comment = commentService.createComment(boardId, commentMapper.commentPostToPost(requestBody, board), principal);
            CommentDto.Response response = commentMapper.commentToPostResponse(comment);
            return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
        }

        @PatchMapping("{board-id}/comments/update")
        public ResponseEntity commentPatch(@PathVariable("board-id") Long postId , @RequestBody CommentDto.Patch requestBody, Principal principal) {
            Comment comment = commentService.updateComment(commentMapper.commentPatchToPost(requestBody), postId, principal);
            CommentDto.Response response = commentMapper.commentToPostResponse(comment);
            return new ResponseEntity<>(
                    new SingleResponseDto<>(response),
                    HttpStatus.OK);
        }

        @GetMapping("{board-id}/comments/{comment-id}")
        public ResponseEntity getComment(@PathVariable("board-id") @Positive Long boardId, @PathVariable("comment-id") @Positive Long commentId) {
            Board board = boardRepository.findByBoardId(boardId);
            Comment comment = commentService.getComment(commentId);
            return new ResponseEntity<>(
                    new SingleResponseDto<>(commentMapper.commentToPostResponse(comment)),
                    HttpStatus.OK);
        }

        @DeleteMapping("{board-id}/comments/{comment-id}")
        public ResponseEntity deleteComment(@PathVariable("board-id") Long postId, @PathVariable("comment-id") Long commentId, Principal principal) {
            commentService.deleteComment(commentId, postId, principal);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
        }
}
