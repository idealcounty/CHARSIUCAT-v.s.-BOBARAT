package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.User;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.service.UserService;
import com.example.SBEAM.util.TokenUtil;
import com.example.SBEAM.vo.UserVO;
import com.example.SBEAM.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;


    @Override
    public Boolean register(UserVO userVO) {
        User user = userRepository.findByPhone(userVO.getPhone());
        if (user != null) {
            throw SBEAMException.phoneAlreadyExists();
        }
        User newUser = userVO.toPO();
        newUser.setCreateTime(new Date());
        userRepository.save(newUser);
        return true;
    }

    @Override
    public String login(String phone, String password) {
        User user = userRepository.findByPhoneAndPassword(phone, password);
        if (user == null) {
            throw SBEAMException.phoneOrPasswordError();
        }
        return tokenUtil.getToken(user);
    }

    @Override
    public UserVO getInformation() {
        User user=securityUtil.getCurrentUser();
        return user.toVO();
    }

    @Override
    public Boolean updateInformation(UserVO userVO) {
        User user=securityUtil.getCurrentUser();
        if (userVO.getPassword()!=null){
            user.setPassword(userVO.getPassword());
        }
        if (userVO.getName()!=null){
            user.setName(userVO.getName());
        }
        if (userVO.getAddress()!=null){
            user.setAddress(userVO.getAddress());
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public void depositBalance(Integer userId, double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.updateBalance(amount);
    }
}

