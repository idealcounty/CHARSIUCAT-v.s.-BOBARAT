package com.example.SBEAM.controller;
import com.example.SBEAM.service.UserService;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResultVO<Boolean> register(@RequestBody UserVO userVO){
        return ResultVO.buildSuccess(userService.register(userVO));
    }

    @PostMapping("/login")
    public ResultVO<String> login(@RequestParam("phone") String phone, @RequestParam("password") String password){
        return ResultVO.buildSuccess(userService.login(phone, password));
    }

    @GetMapping
    public ResultVO<UserVO> getInformation(){
        return ResultVO.buildSuccess(userService.getInformation());
    }

    @PutMapping ("/{userId}")
    public ResultVO<Boolean> updateInformation(@PathVariable int userId,@RequestBody UserVO userVO){
        boolean success = userService.updateInformation(userId,userVO);
        if (success) {
            return ResultVO.buildSuccess(true);
        }
        else {
            return ResultVO.buildFailure("请求信息失败");
        }
    }

//    @PutMapping("/{userId}")
//    public ResultVO<Void> deposit(@PathVariable int userId, @RequestParam double amount){
//        userService.depositBalance(userId,amount);
//        return ResultVO.buildSuccess();
//    }
}
