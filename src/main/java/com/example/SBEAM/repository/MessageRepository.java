package com.example.SBEAM.repository;

import com.example.SBEAM.po.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.messageSentTime ASC")
    List<Message> findChatHistory(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);
}
