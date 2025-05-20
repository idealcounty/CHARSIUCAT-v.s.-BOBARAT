package com.example.SBEAM.repository;

import com.example.SBEAM.po.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
