package com.example.SBEAM.repository;

import com.example.SBEAM.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
