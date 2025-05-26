package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.User;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    UserRepository userRepository;
    @Override
    public Boolean sendFriendRequest(int selfId, int friendId){
        User self=userRepository.findById(selfId).get();
        User friend= userRepository.findById(friendId).get();
        if(self.getId()==friend.getId()){
            return false;
        }
        for(User friendUser : friend.getAlreadyFriends()){
            if(friendUser.equals(self)){
                return false;
            }
        }
        for(User friendUser : friend.getPreparedFriends()){
            if(friendUser.equals(self)){
                return false;
            }
        }
        friend.getPreparedFriends().add(self);
        userRepository.save(self);
        userRepository.save(friend);
        return true;
    }
    @Override
    public Boolean acceptFriendRequest(int selfId, int friendId){
        User self=userRepository.findById(selfId).get();
        User friend= userRepository.findById(friendId).get();
        self.getAlreadyFriends().add(friend);
        friend.getAlreadyFriends().add(self);
        self.getPreparedFriends().remove(friend);
        userRepository.save(self);
        userRepository.save(friend);
        return true;
    }
    @Override
    public Boolean rejectFriendRequest(int selfId, int friendId){
        User self=userRepository.findById(selfId).get();
        User friend= userRepository.findById(friendId).get();
        self.getPreparedFriends().remove(friend);
        userRepository.save(self);
        userRepository.save(friend);
        return true;
    }
    @Override
    public List<User> showAllFriend(int userId){
        User user= userRepository.findById(userId).get();
        return user.getAlreadyFriends();
    }
    @Override
    public List<User> showFriendRequest(int userId){
        User user= userRepository.findById(userId).get();
        return user.getPreparedFriends();
    }
}
