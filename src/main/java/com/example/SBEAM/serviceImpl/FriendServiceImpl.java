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
    public Boolean sendFriendRequest(User self, User friend){
        for(User friendUser : self.getAlreadyFriends()){
            if(friendUser.equals(friend)){
                throw SBEAMException.friendRequestAlreadySend();
            }
        }
        self.getPreparedFriends().add(friend);
        friend.getPreparedFriends().add(self);
        userRepository.save(self);
        userRepository.save(friend);
        return true;
    }
    @Override
    public Boolean acceptFriendRequest(User self, User friend){
        self.getAlreadyFriends().add(friend);
        friend.getAlreadyFriends().add(self);
        self.getPreparedFriends().remove(friend);
        self.getPreparedFriends().remove(self);
        userRepository.save(self);
        userRepository.save(friend);
        return true;
    }
    @Override
    public Boolean rejectFriendRequest(User self, User friend){
        self.getPreparedFriends().remove(self);
        self.getPreparedFriends().remove(self);
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
