package com.sns.comment.service;

import com.sns.board.entity.Board;
import com.sns.comment.entity.Comment;
import com.sns.comment.repository.CommentRepository;
import com.sns.common.exception.BusinessLogicException;
import com.sns.common.exception.ExceptionCode;
import com.sns.member.domain.entity.Member;
import com.sns.member.domain.repository.MemberRepository;
import com.sns.board.repository.BoardRepository;
import com.sns.board.service.BoardService;
import com.sns.member.domain.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BoardRepository boardRepository;


    private final BoardService boardService;


    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository, MemberService memberService, BoardRepository boardRepository, BoardService boardService) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    public Comment createComment(Long boardId, Comment comment, Principal principal) {
        Member member = memberService.findVerifiedMemberByEmail(principal.getName());
        Board board = boardService.getBoard(boardId);
        comment.setMember(member);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setBoard(board);
        return commentRepository.save(comment);

    }


    public Comment updateComment(Comment comment, Long boardId, Principal principal) {
        Board board = boardService.getBoard(boardId);
        Comment findComment = commentRepository.findById(comment.getCommentId()).get();
        verifyMemberConfirm(findComment, principal);
        findComment.setComment(comment.getComment());
        Comment saved = commentRepository.save(findComment);
        comment.setModifiedAt(saved.getBoard().getModifiedAt());
        return saved;
    }



    public void deleteComment(Long commentId, Long boardId, Principal principal){
        Board board = boardService.getBoard(boardId);
        Comment findComment = commentRepository.findById(commentId).get();
        verifyMemberConfirm(findComment, principal);
       commentRepository.delete(findComment);


    }
    public Comment getComment(Long commentId) {
        Comment comment = findVerifiedComment(commentId);
        return comment;
    }


    public Comment findVerifiedComment(long commentId) {
        Optional<Comment> optionalQuestion = commentRepository.findById(commentId);
        return optionalQuestion.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

    public void verifyMemberConfirm(Comment comment, Principal principal) {
        if (!Objects.equals(principal.getName(), comment.getMember().getEmail())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    //답변 조회
    public Page<Comment> readComments(Board board, int page, int size) {
        Page<Comment> comments = commentRepository.findCommentsByBoard(
                PageRequest.of(page, size, Sort.by("commentId").descending()), board);
        return comments;
    }
}
