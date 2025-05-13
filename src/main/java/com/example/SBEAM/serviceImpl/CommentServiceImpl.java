package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.Comment;
import com.example.SBEAM.repository.CommentRepository;
import com.example.SBEAM.service.CommentService;
import com.example.SBEAM.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Override
    public Boolean createComment(CommentVO commentVO){
        Comment comment = commentRepository.findByCommentId(commentVO.getCommentId());
        if(comment != null){
            throw SBEAMException.commentAlreadyExist();
        }
        Comment newComment = commentVO.toPO();
        commentRepository.save(newComment);
        return true;
    }

    @Override
    public Boolean deleteComment(int commentId){
        Comment comment = commentRepository.findByCommentId(commentId);
        if(comment == null){
            throw SBEAMException.commentNotExist();
        }
        commentRepository.delete(comment);
        return true;
    }
}
