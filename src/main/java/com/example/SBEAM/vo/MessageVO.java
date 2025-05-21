package com.example.SBEAM.vo;

import com.example.SBEAM.po.Message;
import com.example.SBEAM.po.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageVO {
    private int messageId;

    private User sender;

    private User receiver;

    private String messageContent;

    private LocalDateTime messageSentTime;
    public Message toPO(){
        Message message = new Message();
        message.setMessageId(messageId);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageContent(messageContent);
        message.setMessageSentTime(messageSentTime);
        return message;
    }
}
