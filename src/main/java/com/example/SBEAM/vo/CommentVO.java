package com.example.SBEAM.vo;

import com.example.SBEAM.po.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVO {
    private Integer commentId;
    private String commentText;
    private int commentScore;
    public Comment toPO(){
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setCommentText(commentText);
        comment.setCommentScore(commentScore);
        return comment;
    }
}
