package com.example.SBEAM.controller;
import com.example.SBEAM.service.UserService;
import com.example.SBEAM.vo.InventoryVO;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/dashboard/{userId}")
    public ResultVO<UserVO> getUserInformation(@PathVariable("userId") Integer userId){
        return ResultVO.buildSuccess(userService.getUserInformation(userId));
    }
    @PostMapping ("/{userId}")
    public ResultVO<Boolean> updateInformation(@PathVariable int userId,@RequestBody UserVO userVO){
        boolean success = userService.updateInformation(userId,userVO);
        if (success) {
            return ResultVO.buildSuccess(true);
        }
        else {
            return ResultVO.buildFailure("请求信息失败");
        }
    }
    @GetMapping("/{userId}/Inventory")
    public ResultVO<List<InventoryVO>> getInventory(@PathVariable int userId){
        return ResultVO.buildSuccess(userService.getInventory(userId));
    }
//    @PutMapping("/{userId}")
//    public ResultVO<Void> deposit(@PathVariable int userId, @RequestParam double amount){
//        userService.depositBalance(userId,amount);
//        return ResultVO.buildSuccess();
//    }
    @GetMapping("/search/{userPhone}")
    public ResultVO<UserVO> searchUser(@PathVariable String userPhone){
        return ResultVO.buildSuccess(userService.getUserByPhone(userPhone));
    }

    @GetMapping("/get/{userId}")
    public ResultVO<UserVO> searchUser(@PathVariable int userId){
        return ResultVO.buildSuccess(userService.getUserById(userId));
    }
}
