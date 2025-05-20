package com.example.SBEAM.service;

import com.example.SBEAM.po.Message;
import com.example.SBEAM.vo.MessageVO;

import java.util.List;

public interface MessageService {
    Boolean sendMessage(MessageVO messageVO);
    List<MessageVO> getMessages(Integer userId1, Integer userId2);
}
