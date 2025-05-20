package com.example.SBEAM.controller;

import com.example.SBEAM.service.MessageService;
import com.example.SBEAM.vo.MessageVO;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageService messageService;
    @PostMapping("/send")
    public ResultVO<Boolean> sendMessage(@RequestBody MessageVO messageVO) {
        return ResultVO.buildSuccess(messageService.sendMessage(messageVO));
    }
    @GetMapping
    public ResultVO<List<MessageVO>> getMessages(Integer userId1, Integer userId2) {
        return ResultVO.buildSuccess(messageService.getMessages(userId1, userId2));
    }
}
