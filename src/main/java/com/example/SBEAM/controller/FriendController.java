package com.example.SBEAM.controller;
import com.example.SBEAM.po.User;
import com.example.SBEAM.service.FriendService;
import com.example.SBEAM.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
    @Autowired
    FriendService friendService;
    @PostMapping
    public ResultVO<Boolean> sendFriendRequest(@RequestBody User self, @RequestBody User friend) {
        return ResultVO.buildSuccess(friendService.sendFriendRequest(self, friend));
    }
    @PostMapping("/friendRequest")
    public ResultVO<Boolean> acceptFriendRequest(@RequestBody User self, @RequestBody User friend) {
        return ResultVO.buildSuccess(friendService.acceptFriendRequest(self, friend));
    }
    @DeleteMapping("/friendRequest")
    public ResultVO<Boolean> rejectFriendRequest(@RequestBody User self, @RequestBody User friend) {
        return ResultVO.buildSuccess(friendService.rejectFriendRequest(self, friend));
    }
    @GetMapping
    public ResultVO<List<User>> showAllFriend(@RequestParam int userId) {
        return ResultVO.buildSuccess(friendService.showAllFriend(userId));
    }
    @GetMapping
    public ResultVO<List<User>> showFriendRequest(@RequestParam int userId) {
        return ResultVO.buildSuccess(friendService.showFriendRequest(userId));
    }

}
