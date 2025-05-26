package com.example.SBEAM.service;

import com.example.SBEAM.po.User;

import java.util.List;

public interface FriendService {
    Boolean sendFriendRequest(int selfId, int friendId);
    Boolean acceptFriendRequest(int selfId, int friendId);
    Boolean rejectFriendRequest(int selfId, int friendId);
    List<User> showAllFriend(int userId);
    List<User> showFriendRequest(int userId);
}
