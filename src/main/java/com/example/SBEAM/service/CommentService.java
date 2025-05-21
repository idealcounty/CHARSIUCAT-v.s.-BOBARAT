package com.example.SBEAM.service;

import com.example.SBEAM.po.Comment;
import com.example.SBEAM.vo.CommentVO;

public interface CommentService {
    Comment createComment(CommentVO commentVO);
    Boolean deleteComment(int commentId);
}
