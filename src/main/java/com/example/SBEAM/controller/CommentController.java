package com.example.SBEAM.controller;
import com.example.SBEAM.service.CommentService;
import com.example.SBEAM.vo.CommentVO;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResultVO<Boolean> createComment(@RequestBody CommentVO commentVO) {
        return ResultVO.buildSuccess(commentService.createComment(commentVO));
    }
    @DeleteMapping("/{commentId}")
    public ResultVO<Boolean> deleteComment(@PathVariable int commentId) {
        return ResultVO.buildSuccess(commentService.deleteComment(commentId));
    }

}
