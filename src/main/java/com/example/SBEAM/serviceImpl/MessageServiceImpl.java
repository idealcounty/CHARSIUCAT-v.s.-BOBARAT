package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.CartItem;
import com.example.SBEAM.po.Message;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.vo.MessageVO;
import com.example.SBEAM.po.User;
import com.example.SBEAM.repository.MessageRepository;
import com.example.SBEAM.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Boolean sendMessage(MessageVO messageVO){
        User Sender = messageVO.getSender();
        User Receiver = messageVO.getReceiver();
        User sender =userRepository.findById(Sender.getId()).get();
        User receiver =userRepository.findById(Receiver.getId()).get();
        if(sender.getAlreadyFriends()==null){
            return false;
        }
        if (!sender.getAlreadyFriends().contains(receiver)) {
            throw SBEAMException.friendNotExist();
        }
        Message message = messageVO.toPO();
        message.setMessageSentTime(LocalDateTime.now());
        messageRepository.save(message);
        return true;
    }
    @Override
    public List<MessageVO> getMessages(Integer userId1, Integer userId2){
        return messageRepository.findChatHistory(userId1,userId2).stream().map(Message::toVO).collect(Collectors.toList());
    }
}
