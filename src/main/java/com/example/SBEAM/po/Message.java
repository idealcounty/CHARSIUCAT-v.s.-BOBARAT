package com.example.SBEAM.po;

import com.example.SBEAM.vo.MessageVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT")
    private String messageContent;

    @Column(name = "message_sent_time", nullable = false)
    private LocalDateTime messageSentTime;

    public MessageVO toVO(){
        MessageVO messageVO = new MessageVO();
        messageVO.setMessageId(messageId);
        messageVO.setSender(sender);
        messageVO.setReceiver(receiver);
        messageVO.setMessageContent(messageContent);
        messageVO.setMessageSentTime(messageSentTime);
        return messageVO;
    }
}