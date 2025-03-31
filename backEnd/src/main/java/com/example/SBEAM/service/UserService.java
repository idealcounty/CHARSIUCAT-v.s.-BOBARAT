package com.example.SBEAM.service;
import com.example.SBEAM.vo.UserVO;


public interface UserService {
    Boolean register(UserVO userVO);

    String login(String phone,String password);

    UserVO getInformation();

    Boolean updateInformation(UserVO userVO);

    void depositBalance(Integer userId, double amount);
}