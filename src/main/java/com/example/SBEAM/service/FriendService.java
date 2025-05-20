package com.example.SBEAM.service;

import com.example.SBEAM.po.User;

import java.util.List;

public interface FriendService {
    Boolean sendFriendRequest(User self, User friend);
    Boolean acceptFriendRequest(User self, User friend);
    Boolean rejectFriendRequest(User self, User friend);
    List<User> showAllFriend(int userId);
    List<User> showFriendRequest(int userId);
}
