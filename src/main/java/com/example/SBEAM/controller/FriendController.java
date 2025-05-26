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
    public ResultVO<Boolean> sendFriendRequest(@RequestParam int selfId, @RequestParam int friendId) {
        return ResultVO.buildSuccess(friendService.sendFriendRequest(selfId, friendId));
    }
    @PostMapping("/friendRequest")
    public ResultVO<Boolean> acceptFriendRequest(@RequestParam int selfId, @RequestParam int friendId) {
        return ResultVO.buildSuccess(friendService.acceptFriendRequest(selfId, friendId));
    }
    @DeleteMapping("/friendRequest")
    public ResultVO<Boolean> rejectFriendRequest(@RequestParam int selfId, @RequestParam int friendId) {
        return ResultVO.buildSuccess(friendService.rejectFriendRequest(selfId, friendId));
    }
    @GetMapping
    public ResultVO<List<User>> showAllFriend(@RequestParam int userId) {
        return ResultVO.buildSuccess(friendService.showAllFriend(userId));
    }
    @GetMapping("/friendRequest")
    public ResultVO<List<User>> showFriendRequest(@RequestParam int userId) {
        return ResultVO.buildSuccess(friendService.showFriendRequest(userId));
    }

}
