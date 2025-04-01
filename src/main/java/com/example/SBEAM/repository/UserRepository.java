package com.example.SBEAM.repository;

import com.example.SBEAM.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String phone);

    User findByPhoneAndPassword(String phone, String password);

}
