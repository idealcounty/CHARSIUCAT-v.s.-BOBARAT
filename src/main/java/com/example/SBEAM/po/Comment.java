package com.example.SBEAM.po;
import com.example.SBEAM.vo.CommentVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Basic
    @Column(name = "comment_text",columnDefinition = "TEXT")
    private String commentText;

    @Basic
    @Column(name = "comment_score")
    private boolean commentScore;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "comment_send_time")
    private Date commentSendTime;

    public Comment (String commentText, boolean commentScore) {
        this.commentText = commentText;
        this.commentScore = commentScore;

    }
    public CommentVO toVO(){
        CommentVO commentVO = new CommentVO();
        commentVO.setCommentId(commentId);
        commentVO.setCommentText(commentText);
        commentVO.setCommentScore(commentScore);
        commentVO.setProduct(product);
        commentVO.setUserId(userId);
        return commentVO;
    }
}
