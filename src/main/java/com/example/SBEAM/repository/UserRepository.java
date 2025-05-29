package com.example.SBEAM.repository;

import com.example.SBEAM.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String phone);

    Optional<User> findById(Integer Id);
}
