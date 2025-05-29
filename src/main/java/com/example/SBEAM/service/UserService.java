package com.example.SBEAM.service;

import com.example.SBEAM.po.Inventory;
import com.example.SBEAM.vo.InventoryVO;
import com.example.SBEAM.vo.UserVO;

import java.util.List;

public interface UserService {
    Boolean register(UserVO userVO);

    String login(String phone, String password);

    UserVO getInformation();

    Boolean updateInformation(int userId, UserVO userVO);

    void depositBalance(Integer userId, double amount);

    List<InventoryVO> getInventory(int userId);

    UserVO getUserInformation(int userId);

    UserVO getUserByPhone(String phone);

    UserVO getUserById(int userId);

    Integer getUserGameCount(int userId);
}