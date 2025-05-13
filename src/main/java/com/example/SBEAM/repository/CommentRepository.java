package com.example.SBEAM.repository;

import com.example.SBEAM.po.Comment;
import com.example.SBEAM.po.Product;
import com.example.SBEAM.vo.CommentVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findByCommentId(int commentId);
    List<Comment> findCommentsByProduct(Product product);
    List<Comment> findByProductAndCommentScoreGreaterThanEqual(Product product, int score);
    List<Comment> findByProductAndCommentScoreLessThanEqual(Product product,int score);
}
