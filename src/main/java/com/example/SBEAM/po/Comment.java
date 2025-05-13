package com.example.SBEAM.po;
import com.example.SBEAM.vo.CommentVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

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
    @Column(name = "comment_text")
    private String commentText;

    @Basic
    @Column(name = "comment_score")
    private int commentScore;

    public Comment (String commentText, int commentScore) {
        this.commentText = commentText;
        this.commentScore = commentScore;
    }
    public CommentVO toVO(){
        CommentVO commentVO = new CommentVO();
        commentVO.setCommentId(commentId);
        commentVO.setCommentText(commentText);
        commentVO.setCommentScore(commentScore);
        return commentVO;
    }
}
