package com.example.SBEAM.service;

import com.example.SBEAM.vo.CommentVO;

public interface CommentService {
    Boolean createComment(CommentVO commentVO);
    Boolean deleteComment(int commentId);
}
