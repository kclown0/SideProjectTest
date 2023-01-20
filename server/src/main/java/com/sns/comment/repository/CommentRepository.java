package com.sns.comment.repository;


import com.sns.board.entity.Board;
import com.sns.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentsByBoard(PageRequest of, Board board);
}
