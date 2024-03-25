package com.paperStar.service;

import com.paperStar.pojo.Result;
import com.paperStar.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Result Login(User user);
    Result Logout(int id);
    Result Register(User user);
    Result updateProfile(int id,User user);
}
